package com.example.journalog.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name="DATASTORE_MAIN")

class DataStoreRepositoryImpl @Inject constructor(private val context: Context) : DataStoreRepository {
    private val USERNAME: String = "username"
    private val USER_ID: String = "userId"

    override suspend fun putUsername(username: String) {
        putString(USERNAME, username)
    }

    override suspend fun getUsername(): String? {
        return getString(USERNAME)
    }

    override suspend fun putUserId(userId: Int) {
        putInt(USER_ID, userId)
    }

    override suspend fun getUserId(): Int? {
        return getInt(USER_ID)
    }

    override suspend fun clearUserData() {
        clearString(USERNAME)
        clearInt(USER_ID)
    }


    private suspend fun putString(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    private suspend fun putInt(key: String, value: Int) {
        val preferenceKey = intPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    private suspend fun getString(key: String): String? {
        return try {
            val preferenceKey = stringPreferencesKey(key)
            val preference = context.dataStore.data.first()
            preference[preferenceKey]
        } catch (e:Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun getInt(key: String): Int? {
        return try {
            val preferenceKey = intPreferencesKey(key)
            val preference = context.dataStore.data.first()
            preference[preferenceKey]
        } catch (e:Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun clearString(key: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit {
            if (it.contains(preferenceKey)) {
                it.remove(preferenceKey)
            }
        }
    }

    private suspend fun clearInt(key: String) {
        val preferenceKey = intPreferencesKey(key)
        context.dataStore.edit {
            if (it.contains(preferenceKey)) {
                it.remove(preferenceKey)
            }
        }
    }
}