package com.beetzung.quizgame.data.api.quiz

data class Question(
    val answers: Map<String, String>,
    val text: String
)