package com.beetzung.quizgame.data

import com.beetzung.quizgame.data.model.game.GameResponse
import com.beetzung.quizgame.data.model.quiz.QuizResponse
import kotlinx.coroutines.flow.Flow

interface GameAPI {
    fun connectToGame(token: String, password: String)

    fun disconnectFromGame(password: String)

    fun subscribeToGame(token: String, password: String): Flow<GameResponse>

    fun subscribeToQuestion(token: String, password: String): Flow<QuizResponse>

    fun startGame(token: String, password: String)

    fun answer(token: String, password: String, answer: Int)

    fun requestUpdate(token: String, password: String)
}