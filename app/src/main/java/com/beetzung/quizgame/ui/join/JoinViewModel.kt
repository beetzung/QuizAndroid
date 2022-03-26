package com.beetzung.quizgame.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beetzung.quizgame.data.QuizAPI
import com.beetzung.quizgame.data.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val preferences: Preferences,
    private val quizAPI: QuizAPI
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(JoinState())
    val stateFlow = _stateFlow.asStateFlow()

    fun joinGame(password: String, name: String) {
        viewModelScope.launch {
            _stateFlow.emit(
                with(quizAPI.join(name, password)) {
                    JoinState(
                        success = data?.let { data ->
                            preferences.saveGame(password, data.token)
                            true
                        },
                        error = error
                    )
                }
            )
        }
    }
}