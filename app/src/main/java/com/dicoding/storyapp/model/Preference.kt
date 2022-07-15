package com.dicoding.storyapp.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Preference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUserLogin(): Flow<User> {
        return dataStore.data.map {
            User(
                it[USERID_KEY] ?: "",
                it[NAME_KEY] ?: "",
                it[EMAIL_KEY] ?: "",
                it[PASSWORD_KEY] ?: "",
                it[TOKEN_KEY] ?: "",
                it[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUserLogin(user: User) {
        dataStore.edit {
            it[USERID_KEY] = user.userId
            it[NAME_KEY] = user.name
            it[EMAIL_KEY] = user.email
            it[PASSWORD_KEY] = user.password
            it[TOKEN_KEY] = user.token
            it[STATE_KEY] = user.login
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it[USERID_KEY] = ""
            it[STATE_KEY] = false
            it[NAME_KEY] = ""
            it[EMAIL_KEY] = ""
            it[TOKEN_KEY] = ""
            it[PASSWORD_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Preference? = null

        private val USERID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): Preference {
            return INSTANCE ?: synchronized(this) {
                val instance = Preference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}