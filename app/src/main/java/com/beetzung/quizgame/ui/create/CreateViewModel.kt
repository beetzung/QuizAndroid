package com.beetzung.quizgame.ui.create

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
class CreateViewModel @Inject constructor(
    private val preferences: Preferences,
    private val quizAPI: QuizAPI
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(CreateState())
    val stateFlow = _stateFlow.asStateFlow()

    fun createGame(name: String, number: Int) {
        viewModelScope.launch {
            _stateFlow.emit(
                with(quizAPI.create(name, number)) {
                    CreateState(
                        password = data?.let { data ->
                            preferences.saveGame(
                                data.password,
                                data.token
                            )
                            data.password
                        },
                        error = error
                    )
                }
            )
        }
    }
}