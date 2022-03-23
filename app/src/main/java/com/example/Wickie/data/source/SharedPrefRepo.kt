package com.example.Wickie.data.source

import android.content.Context
import android.content.SharedPreferences

/*
* Const values used for shared preference throughout the application
* */
const val PREF_LOGGED_IN = "PREF_LOGGED_IN"
const val PREF_USERNAME = "PREF_USERNAME"
const val PREF_FINGERPRINT_STATUS = "PREF_FINGERPRINT_STATUS"

class SharedPrefRepo(val context: Context) {
    // ========================================= START OF BOILER CODE FOR SHARED PREFERENCE REPO ============================================
    private val pref: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    private fun String.put(long: Long) {
        editor.putLong(this, long)
        editor.commit()
    }

    private fun String.put(int: Int) {
        editor.putInt(this, int)
        editor.commit()
    }

    private fun String.put(string: String) {
        editor.putString(this, string)
        editor.commit()
    }

    private fun String.put(boolean: Boolean) {
        editor.putBoolean(this, boolean)
        editor.commit()
    }

    private fun String.getLong() = pref.getLong(this, 0)

    private fun String.getInt() = pref.getInt(this, 0)

    private fun String.getString() = pref.getString(this, "")!!

    private fun String.getBoolean() = pref.getBoolean(this, false)

    fun clearData() {
        editor.clear()
        editor.commit()
    }
    // ========================================= END OF BOILER CODE FOR SHARED PREFERENCE REPO ============================================

    fun setLoggedIn(isLoggedIn: Boolean) {
        PREF_LOGGED_IN.put(isLoggedIn)
    }
    fun getLoggedIn() = PREF_LOGGED_IN.getBoolean()

    fun setUsername(username: String) {
        PREF_USERNAME.put(username)
    }
    fun getUsername() = PREF_USERNAME.getString()

    fun setFingerPrintStatus(status: Boolean) {
        PREF_FINGERPRINT_STATUS.put(status)
    }
    fun getFingerPrintStatus() = PREF_FINGERPRINT_STATUS.getBoolean()

    companion object {
        // Constant for naming our DataStore - you can change this if you want
        private const val PREFERENCE_NAME = "sharedPref"

        // The usual for debugging
        private val TAG: String = "UserPreferencesRepository"

        // Boilerplate-y code for singleton: the private reference to this self
        @Volatile
        private var INSTANCE: SharedPrefRepo? = null

        fun getInstance(context: Context): SharedPrefRepo {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = SharedPrefRepo(context)
                INSTANCE = instance
                instance
            }
        }
    }

}