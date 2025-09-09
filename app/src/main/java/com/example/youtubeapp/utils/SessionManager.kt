package com.example.youtubeapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

object SessionManager {

    private const val DATASTORE_NAME = "auth"
    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    private val EMAIL_KEY = stringPreferencesKey("email")

    suspend fun saveUserSession(context: Context, email: String) {
        try {
            context.dataStore.edit { prefs ->
                prefs[EMAIL_KEY] = email
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserSession(context: Context): Flow<String?> =
        context.dataStore.data
            .catch { e -> e.printStackTrace(); emit(emptyPreferences()) }
            .map { prefs -> prefs[EMAIL_KEY] }

    suspend fun clearUserSession(context: Context) {
        try {
            context.dataStore.edit { it.clear() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
