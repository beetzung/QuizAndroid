package com.beetzung.quizgame.data.api.quiz

data class Data(
    val name: String,
    val answer: String,
    val is_admin: Boolean,
    val players: List<String>,
    val question: Question?,
    val score: List<List<Any>>?,
    val winner: String?
)