package com.beetzung.quizgame.data

import com.beetzung.quizgame.data.model.create.CreateResponse
import com.beetzung.quizgame.data.model.join.JoinResponse
import com.beetzung.quizgame.data.model.quiz.QuizResponse

interface LobbyAPI {
    suspend fun create(name: String, players: Int): CreateResponse

    suspend fun join(name: String, password: String): JoinResponse

    @Deprecated(message = "Moved to socket api")
    suspend fun start(token: String, password: String): QuizResponse

    @Deprecated(message = "Moved to socket api")
    suspend fun status(token: String, password: String): QuizResponse

    @Deprecated(message = "Moved to socket api")
    suspend fun answer(token: String, password: String, answer: Int): QuizResponse
}