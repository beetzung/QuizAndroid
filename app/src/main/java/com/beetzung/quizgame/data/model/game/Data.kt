package com.beetzung.quizgame.data.model.game

data class Data(
    val players: List<String>,
    val score: List<Score>?,
    val winner: String?
)

data class Score(val name: String, val score: Double)
