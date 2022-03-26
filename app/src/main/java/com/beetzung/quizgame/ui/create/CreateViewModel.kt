package com.beetzung.quizgame.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beetzung.quizgame.data.api.QuizAPI
import com.beetzung.quizgame.data.prefs.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(CreateState())
    val stateFlow = _stateFlow.asStateFlow()

    fun createGame(name: String, number: Int) {
        viewModelScope.launch {
            val response = QuizAPI.get().create(name, number)
            if (response.isSuccessful) {
                val token =
                    response.body()!!.data!!.token
                val password =
                    response.body()!!.data!!.password
                Preferences.saveGame(
                    password,
                    token
                )
                _stateFlow.emit(CreateState(password = password))
            } else {
                _stateFlow.emit(CreateState(error = response.errorBody().toString()))
            }
        }
    }
}