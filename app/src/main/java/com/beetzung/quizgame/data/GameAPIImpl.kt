package com.beetzung.quizgame.data

import android.util.Log
import com.beetzung.quizgame.data.model.game.GameResponse
import com.beetzung.quizgame.data.model.quiz.QuizResponse
import com.beetzung.quizgame.ui.MainActivity.Companion.TAG
import com.google.gson.Gson
import io.socket.client.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import org.json.JSONObject

class GameAPIImpl : GameAPI {
    companion object {
        private const val PORT = 80
        private const val HOST = "192.168.0.193"
        private const val URL = "http://$HOST:$PORT"
        private const val EVENT_CONNECT = "connect_to_game"
        private const val EVENT_DISCONNECT = "disconnect_game"
        private const val EVENT_START = "start"
        private const val EVENT_REQUEST = "request"
        private const val EVENT_GAME = "game"
        private const val EVENT_QUESTION = "question"
        private const val EVENT_ANSWER = "answer"
    }

    private val socket = IO.socket(URL)
    private val gson = Gson()

    override fun connectToGame(token: String, password: String) {
        socket.connect()
        socket.emit(
            EVENT_CONNECT, getJsonString(
                Pair("password", password), Pair("token", token)
            )
        )
    }

    override fun disconnectFromGame(password: String) {
        socket.emit(EVENT_DISCONNECT, getJsonString(Pair("password", password)))
        socket.disconnect()
    }

    override fun subscribeToGame(token: String, password: String) = callbackFlow {
        socket.on(EVENT_GAME) { args ->
            val response = try {
                val json = args[0] as JSONObject
                Log.d(TAG, "subscribeToGame: $json")
                gson.fromJson(json.toString(), GameResponse::class.java)
            } catch (e: ClassCastException) {
                GameResponse(error = "Server error: response is not a JSON")
            }
            trySendBlocking(response)
        }
        awaitClose {
            socket.off(EVENT_GAME)
        }
    }

    override fun subscribeToQuestion(token: String, password: String) = callbackFlow {
        socket.on(EVENT_QUESTION) { args ->
            val response = try {
                val json = args[0] as JSONObject
                Log.d(TAG, "subscribeToQuestion: $json")
                gson.fromJson(json.toString(), QuizResponse::class.java)
            } catch (e: ClassCastException) {
                QuizResponse(error = "Server error: response is not a JSON")
            }
//            flow.tryEmit(response)
            trySendBlocking(response)
        }
//        return flow
        awaitClose {
            socket.off(EVENT_QUESTION)
        }
    }

    override fun startGame(token: String, password: String) {
        socket.emit(EVENT_START, getJsonString(Pair("password", password), Pair("token", token)))
    }

    override fun answer(token: String, password: String, answer: Int) {
        socket.emit(
            EVENT_ANSWER, getJsonString(
                Pair("token", token),
                Pair("password", password),
                Pair("answer", answer)
            )
        )
    }

    override fun requestUpdate(token: String, password: String) {
        socket.emit(EVENT_REQUEST, getJsonString(Pair("token", token), Pair("password", password)))
    }

    private fun getJsonString(vararg data: Pair<String, Any>) = JSONObject(
        mutableMapOf<Any?, Any?>(
            *data
        )
    ).toString(4)
}

