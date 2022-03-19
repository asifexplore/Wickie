package com.example.Wickie.data.source.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.example.Wickie.features.login.Login
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class UserPreferencesRepository(context: Context) {
    val biometricDatastore: DataStore<Preferences> = context.createDataStore(name = name)

    private object PreferenceKeys {
        val userName = stringPreferencesKey("username")
        val password = stringPreferencesKey("password")
        val biometric = booleanPreferencesKey("biometric")
    }
    /*
    companion object {
        val userName = stringPreferencesKey("username")
        val password = stringPreferencesKey("password")
        val biometric = booleanPreferencesKey("biometric")

    }

     */

    suspend fun saveLogin(login: Login) {
        biometricDatastore.edit { logins->
            logins[PreferenceKeys.userName] = login.userName
            logins[PreferenceKeys.password] = login.password
            logins[PreferenceKeys.biometric] = login.biometric

        }
    }

    suspend fun getLogin() = biometricDatastore.data.map { login ->
        Login(
            userName = login[PreferenceKeys.userName]!!,
            password = login[PreferenceKeys.password]!!,
            biometric = login[PreferenceKeys.biometric]!!,
        )

    }

    val readUsername: Flow<String> = biometricDatastore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map{
                preference ->
            val userName = preference[PreferenceKeys.userName] ?: ""
            userName
        }

    val readPassword: Flow<String> = biometricDatastore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map{
                preference ->
            val password = preference[PreferenceKeys.password] ?: ""
            password
        }
    val readBiometric: Flow<Boolean> = biometricDatastore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map{
                preference ->
            val biometric = preference[PreferenceKeys.biometric] ?: false
            biometric
        }

    companion object {
        // Constant for naming our DataStore - you can change this if you want
        private var name = "biometric_datastore"

        // The usual for debugging
        private val TAG: String = "UserPreferencesRepository"

        // Boilerplate-y code for singleton: the private reference to this self
        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null

        /**
         * Boilerplate-y code for singleton: to ensure only a single copy is ever present
         * @param context to init the datastore
         */
        fun getInstance(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = UserPreferencesRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }



}