package com.beetzung.quizgame.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beetzung.quizgame.data.model.quiz.QuizResponse
import com.beetzung.quizgame.data.Preferences
import com.beetzung.quizgame.data.GameAPI
import com.beetzung.quizgame.data.model.game.GameResponse
import com.beetzung.quizgame.ui.MainActivity.Companion.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val preferences: Preferences,
    private val gameAPI: GameAPI
) : ViewModel() {
    private val _gameFlow = MutableSharedFlow<GameState>()
    val gameFlow = _gameFlow.asSharedFlow()
    private val _questionFlow = MutableSharedFlow<QuestionState>()
    val questionFlow = _questionFlow.asSharedFlow()
    private var refreshJob: Job? = null
    private val password = preferences.getGame()!!
    private val token = preferences.getToken()!!

    fun onStart() {
        gameAPI.connectToGame(token, password)
        viewModelScope.launch {
            gameAPI
                .subscribeToGame(token, password)
                .onEach { response ->
                    handleGameState(response)
                }
                .launchIn(this)
        }
        viewModelScope.launch {
            gameAPI
                .subscribeToQuestion(token, password)
                .onEach { response ->
                    handleQuestionState(response)
                }
                .launchIn(this)
        }
        gameAPI.requestUpdate(token, password)
    }

    fun onStop() {
        gameAPI.disconnectFromGame(password)
    }

    fun refresh() {
        gameAPI.requestUpdate(token, password)
    }

    fun start() {
        gameAPI.startGame(token, password)
    }

    fun answer(index: Int) {
        gameAPI.answer(token, password, index)
    }

    private suspend fun handleQuestionState(response: QuizResponse) {
        Log.d(TAG, "handleQuestionState() called with: response = $response")
        _questionFlow.emit(
            with(response) {
                QuestionState(
                    question = data?.question,
                    name = data?.name,
                    answerIsCorrect = data?.answeredCorrectly,
                    status = when (status) {
                        "started" -> QuestionState.Status.Started
                        "created" -> QuestionState.Status.Created(data!!.is_admin)
                        "finished" -> QuestionState.Status.Finished("data!!.winner!!")  //TODO
                        else -> null
                    },
                )
            }
        )
    }

    private suspend fun handleGameState(response: GameResponse) {
        Log.d(TAG, "handleGameState() called with: response = $response")
        _gameFlow.emit(
            with(response) {
                GameState(
                    refreshing = false,
                    error = error,
                    score = data?.score?.map {
                        Pair(
                            it.name,
                            it.score.toInt().toString()
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