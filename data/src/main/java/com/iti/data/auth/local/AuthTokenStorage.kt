package com.iti.data.auth.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iti.domain.auth.model.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private object Keys {
        val TOKEN = stringPreferencesKey("auth_token")
        val TOKEN_TYPE = stringPreferencesKey("auth_token_type")
        val USER_ID = intPreferencesKey("auth_user_id")
        val USER_NAME = stringPreferencesKey("auth_user_name")
        val USER_EMAIL = stringPreferencesKey("auth_user_email")
        val USER_ROLE = stringPreferencesKey("auth_user_role")
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        !prefs[Keys.TOKEN].isNullOrBlank()
    }

    val authToken: Flow<String?> = dataStore.data.map { it[Keys.TOKEN] }

    val currentUser: Flow<AuthUser?> = dataStore.data.map { prefs ->
        val token = prefs[Keys.TOKEN]
        if (token.isNullOrBlank()) {
            null
        } else {
            AuthUser(
                id = prefs[Keys.USER_ID] ?: 0,
                name = prefs[Keys.USER_NAME].orEmpty(),
                email = prefs[Keys.USER_EMAIL].orEmpty(),
                role = prefs[Keys.USER_ROLE].orEmpty(),
                token = token,
                tokenType = prefs[Keys.TOKEN_TYPE].orEmpty(),
            )
        }
    }

    suspend fun saveSession(user: AuthUser) {
        dataStore.edit { prefs ->
            prefs[Keys.TOKEN] = user.token
            prefs[Keys.TOKEN_TYPE] = user.tokenType
            prefs[Keys.USER_ID] = user.id
            prefs[Keys.USER_NAME] = user.name
            prefs[Keys.USER_EMAIL] = user.email
            prefs[Keys.USER_ROLE] = user.role
        }
    }

    suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.TOKEN)
            prefs.remove(Keys.TOKEN_TYPE)
            prefs.remove(Keys.USER_ID)
            prefs.remove(Keys.USER_NAME)
            prefs.remove(Keys.USER_EMAIL)
            prefs.remove(Keys.USER_ROLE)
        }
    }
}