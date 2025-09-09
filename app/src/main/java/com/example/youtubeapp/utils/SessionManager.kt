package com.example.youtubeapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SessionManager {

    private const val PREF_NAME = "auth_prefs"
    private const val KEY_EMAIL = "email"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserSession(context: Context, email: String) {
        getPrefs(context).edit {
            putString(KEY_EMAIL, email)
        }
    }

    fun getUserSession(context: Context): String? {
        return getPrefs(context).getString(KEY_EMAIL, null)
    }

    fun clearUserSession(context: Context) {
        getPrefs(context).edit {
            remove(KEY_EMAIL)
        }
    }
}
