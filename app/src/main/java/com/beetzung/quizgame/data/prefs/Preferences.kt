package com.beetzung.quizgame.data.prefs

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private lateinit var prefs: SharedPreferences
    const val KEY_GAME = "game"
    const val KEY_TOKEN = "token"
    fun init(activity: Activity) {
        prefs = activity.getPreferences(Context.MODE_PRIVATE)
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