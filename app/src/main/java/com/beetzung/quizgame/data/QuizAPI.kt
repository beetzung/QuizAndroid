package com.beetzung.quizgame.data

import com.beetzung.quizgame.data.retrofit_api_impl.create.CreateResponse
import com.beetzung.quizgame.data.retrofit_api_impl.join.JoinResponse
import com.beetzung.quizgame.data.retrofit_api_impl.quiz.QuizResponse

interface QuizAPI {
    suspend fun create(name: String, players: Int): CreateResponse

    suspend fun join(name: String, password: String): JoinResponse

    suspend fun start(token: String, password: String): QuizResponse

    suspend fun status(token: String, password: String): QuizResponse

    suspend fun answer(token: String, password: String, answer: Int): QuizResponse
}