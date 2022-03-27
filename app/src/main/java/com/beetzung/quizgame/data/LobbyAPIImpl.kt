package com.beetzung.quizgame.data

import com.beetzung.quizgame.data.model.create.CreateResponse
import com.beetzung.quizgame.data.model.join.JoinResponse
import com.beetzung.quizgame.data.model.quiz.QuizResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class LobbyAPIImpl : LobbyAPI {
    companion object {
        private var BASE_URL = "https://quiz.beetzung.com/"
        private var LOCAL_URL = "http://192.168.0.193:80/"
    }

    interface RetrofitQuizAPI {
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
    }

    val retofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(Utils.getUnsafeOkHttpClient().build())
        .baseUrl(BASE_URL)
        .build()
        .create(RetrofitQuizAPI::class.java)


    override suspend fun create(name: String, players: Int): CreateResponse {
        val response = retofit.create(name, players)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            CreateResponse(error = response.errorString)
        }
    }

    override suspend fun join(name: String, password: String): JoinResponse {
        val response = retofit.join(name, password)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            JoinResponse(error = response.errorString)
        }
    }

    @Deprecated(message = "Moved to socket api")
    override suspend fun start(token: String, password: String): QuizResponse {
        val response = retofit.start(token, password)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            QuizResponse(error = response.errorString)
        }
    }

    @Deprecated(message = "Moved to socket api")
    override suspend fun status(token: String, password: String): QuizResponse {
        val response = retofit.status(token, password)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            QuizResponse(error = response.errorString)
        }
    }

    @Deprecated(message = "Moved to socket api")
    override suspend fun answer(token: String, password: String, answer: Int): QuizResponse {
        val response = retofit.answer(token, password, answer)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            QuizResponse(error = response.errorString)
        }
    }

    val <T> Response<T>.errorString
    get() = this.errorBody().toString()

    val <T> Response<T>.exception
    get() = Exception(errorString)
}