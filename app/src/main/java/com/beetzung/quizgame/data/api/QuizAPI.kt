package com.beetzung.quizgame.data.api

import com.beetzung.quizgame.data.Utils
import com.beetzung.quizgame.data.api.create.CreateResponse
import com.beetzung.quizgame.data.api.join.JoinResponse
import com.beetzung.quizgame.data.api.quiz.QuizResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizAPI {
    @GET("/create")
    suspend fun create(
        @Query("name") name: String,
        @Query("players") players: Int
    ): Response<CreateResponse>

    @GET("join")
    suspend fun join(
        @Query("name") name: String,
        @Query("password") password: String
    ): Response<JoinResponse>

    @GET("start")
    suspend fun start(
        @Query("token") token: String,
        @Query("password") password: String
    ): Response<QuizResponse>

    @GET("game")
    suspend fun status(
        @Query("token") token: String,
        @Query("password") password: String
    ): Response<QuizResponse>

    @GET("answer")
    suspend fun answer(
        @Query("token") token: String,
        @Query("password") password: String,
        @Query("answer") answer: Int
    ): Response<QuizResponse>

    companion object {
        private var BASE_URL = "https://quiz.beetzung.com/"
        private var LOCAL_URL = "http://192.168.0.193:80/"

        fun get(): QuizAPI {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(Utils.getUnsafeOkHttpClient().build())
                .baseUrl(LOCAL_URL)
                .build()
            return retrofit.create(QuizAPI::class.java)
        }
    }
}