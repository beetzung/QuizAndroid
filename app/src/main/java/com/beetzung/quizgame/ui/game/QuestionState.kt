package com.beetzung.quizgame.ui.game

import com.beetzung.quizgame.data.model.quiz.Question

data class QuestionState(
//    val isAdmin: Boolean,
    val error: String? = null,
    val name: String? = null,
    val status: Status? = null,
    val question: Question? = null,
    val answerIsCorrect: Boolean? = null,
)  {
    sealed class Status {
        data class Created(val isAdmin: Boolean) : Status()
        object Started: Status()
        data class Finished(val winner: String): Status()
    }
}
