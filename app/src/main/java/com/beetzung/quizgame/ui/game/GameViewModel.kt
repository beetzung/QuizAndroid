package com.beetzung.quizgame.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beetzung.quizgame.data.QuizAPI
import com.beetzung.quizgame.data.retrofit_api_impl.quiz.QuizResponse
import com.beetzung.quizgame.data.Preferences
import com.beetzung.quizgame.ui.MainActivity.Companion.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val preferences: Preferences,
    private val quizAPI: QuizAPI
) : ViewModel() {
    private val _gameFlow = MutableSharedFlow<GameState>()
    val gameFlow = _gameFlow.asSharedFlow()
    private var refreshJob: Job? = null

    fun refresh() {
        refreshJob?.cancel()
        viewModelScope.launch {
            val response =
                quizAPI.status(preferences.getToken()!!, preferences.getGame()!!)
            handleGameState(response)
        }
    }

    fun start() {
        viewModelScope.launch {
            val response = quizAPI.start(preferences.getToken()!!, preferences.getGame()!!)
            handleGameState(response)
        }
    }

    fun answer(index: Int) {
        viewModelScope.launch {
            val response =
                quizAPI.answer(preferences.getToken()!!, preferences.getGame()!!, index + 1)
            handleGameState(response)
        }
    }

    private suspend fun handleGameState(response: QuizResponse) {
        Log.d(TAG, "handleGameState() called with: response = $response")
        _gameFlow.emit(
            with(response) {
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
            }
        )
    }

    fun reset() = preferences.reset()

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
        refreshJob = null
    }
}