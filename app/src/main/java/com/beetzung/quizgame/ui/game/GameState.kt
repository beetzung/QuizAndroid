package com.beetzung.quizgame.ui.game

data class GameState(
    val refreshing: Boolean? = null,
    val players: List<String>? = null,
    val score: List<Pair<String, String>>? = null,
    val error: String? = null
)