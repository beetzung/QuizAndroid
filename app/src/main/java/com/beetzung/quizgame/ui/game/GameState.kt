package com.beetzung.quizgame.ui.game

import com.beetzung.quizgame.data.api.quiz.Question

data class GameState(
    val refreshing: Boolean? = null,
    val error: String? = null,
    val players: List<String>? = null,
    val question: Question? = null,
    val status: Status? = null,
    val name: String? = null,
    val score: List<Pair<String, String>>? = null,
    val answerIsCorrect: Boolean? = null,
) {
    sealed class Status {
        data class Created(val isAdmin: Boolean) : Status()
        object Started: Status()
        data class Finished(val winner: String): Status()
    }
}