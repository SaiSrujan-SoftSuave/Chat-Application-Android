package dev.open.chat_application.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SessionManager(private val dataStore: DataStore<Preferences>) {
    private val scope = CoroutineScope(Dispatchers.IO)

    // Expose login state as a Flow<Boolean>
    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { pref -> pref[PreferencesKeys.LOGGED_IN] ?: false }

    // Expose access token as a StateFlow<String> backed by DataStore
    val accessToken: StateFlow<String> = dataStore.data
        .map { pref -> pref[PreferencesKeys.ACCESS_TOKEN] ?: "" }
        .stateIn(scope, SharingStarted.Eagerly, "")

    // Expose user ID as a Flow<String>
    val userId: Flow<String> = dataStore.data
        .map { pref -> pref[PreferencesKeys.USER_ID] ?: "" }

    // Store only the access token in DataStore, and mark as logged in
    suspend fun upsertSession(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOGGED_IN] = true
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
        }
    }

    // Store user ID separately
    suspend fun upsertUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    // Clear all stored session data
    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Update both access token and user ID in one transaction
    suspend fun updateLoginSession(accessToken: String, userId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOGGED_IN] = true
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }
}

object PreferencesKeys {
    val LOGGED_IN = booleanPreferencesKey("logged_in")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val USER_ID = stringPreferencesKey("user_id")
}
