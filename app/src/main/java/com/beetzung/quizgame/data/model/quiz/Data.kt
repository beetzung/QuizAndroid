package com.beetzung.quizgame.data.model.quiz

data class Data(
    val name: String,
    val answeredCorrectly: Boolean?,
    val is_admin: Boolean,
    val question: Question?
)