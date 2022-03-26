package com.beetzung.quizgame.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beetzung.quizgame.data.api.QuizAPI
import com.beetzung.quizgame.data.api.quiz.QuizResponse
import com.beetzung.quizgame.data.prefs.Preferences
import com.beetzung.quizgame.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class GameViewModel : ViewModel() {
    private val _gameFlow = MutableSharedFlow<GameState>()
    val gameFlow = _gameFlow.asSharedFlow()
    private var refreshJob: Job? = null

    fun refresh() {
        refreshJob?.cancel()
        viewModelScope.launch {
            val response =
                QuizAPI.get().status(Preferences.getToken()!!, Preferences.getGame()!!)
            handleGameState(response)
        }
    }

    fun start() {
        viewModelScope.launch {
            val response = QuizAPI.get().start(Preferences.getToken()!!, Preferences.getGame()!!)
            handleGameState(response)
        }
    }

    fun answer(index: Int) {
        viewModelScope.launch {
            val response = QuizAPI.get()
                .answer(Preferences.getToken()!!, Preferences.getGame()!!, index + 1)
            handleGameState(response)
        }
    }

    private suspend fun handleGameState(response: Response<QuizResponse>) {
        Log.d(TAG, "handleGameState() called with: response = $response")
        Log.d(TAG, "handleGameState: ${response.body()}")
        _gameFlow.emit(
            if (response.isSuccessful) {
                response.body()?.run {
                    GameState(
                        refreshing = false,
                        error = error,
                        question = data?.question,
                        name = data?.name,
                        answerIsCorrect = when (data?.answer) {
                            "answer_correct" -> true
                            "answer_incorrect" -> false
                            else -> null
                        },
                        status = when (status) {
                            "started" -> GameState.Status.Started
                            "created" -> GameState.Status.Created(data!!.is_admin)
                            "finished" -> GameState.Status.Finished(data!!.winner!!)
                            else -> null
                        },
                        score = data?.score?.map {
                            Pair(
                                it[0].toString(),
                                (it[1] as Double).toInt().toString()
                            )
                        },
                        players = data?.players
                    )
                } ?: GameState(refreshing = false, error = response.errorBody().toString())
            } else {
                GameState(refreshing = false, error = response.errorBody().toString())
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
        refreshJob = null
    }
}