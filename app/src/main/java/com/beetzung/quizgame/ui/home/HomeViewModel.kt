package com.beetzung.quizgame.ui.home

import androidx.lifecycle.ViewModel
import com.beetzung.quizgame.data.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val preferences: Preferences): ViewModel() {
    fun checkGame() = preferences.getGame() != null
}