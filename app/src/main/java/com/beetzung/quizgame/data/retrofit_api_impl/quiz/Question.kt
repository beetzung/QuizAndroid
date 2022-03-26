package com.beetzung.quizgame.data.retrofit_api_impl.quiz

data class Question(
    val answers: Map<String, String>,
    val text: String
)