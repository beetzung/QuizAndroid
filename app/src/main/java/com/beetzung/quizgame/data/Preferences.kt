package com.beetzung.quizgame.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class Preferences(context: Context) {
    private var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val KEY_GAME = "game"
        private const val KEY_TOKEN = "token"
    }

    fun getGame(): String? = prefs.getString(KEY_GAME, null)
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun saveGame(game: String, token: String) {
        prefs.edit().putString(KEY_GAME, game).putString(KEY_TOKEN, token).apply()
    }

    fun reset() {
        prefs.edit().putString(KEY_GAME, null).putString(KEY_TOKEN, null).apply()
    }
}