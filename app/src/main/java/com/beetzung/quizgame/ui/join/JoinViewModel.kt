package com.beetzung.quizgame.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beetzung.quizgame.data.api.QuizAPI
import com.beetzung.quizgame.data.prefs.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JoinViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(JoinState())
    val stateFlow = _stateFlow.asStateFlow()

    fun joinGame(password: String, name: String) {
        viewModelScope.launch {
            val response = QuizAPI.get().join(
                name,
                password
            )
            if (response.isSuccessful) {
                if (response.body()!!.error != null) {
                    _stateFlow.emit(JoinState(error = response.body()!!.error))
                    return@launch
                }
                val token =
                    response.body()!!.data!!.token
                Preferences.saveGame(
                    password,
                    token
                )
                _stateFlow.emit(JoinState(success = true))
            } else {
                _stateFlow.emit(JoinState(error = response.errorBody().toString()))
            }
        }
    }
}