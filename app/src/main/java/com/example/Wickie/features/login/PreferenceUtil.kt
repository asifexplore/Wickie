package com.example.Wickie.features.login

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

//class PreferenceUtil private constructor(context: Context) {
class PreferenceUtil private constructor(context: Context) {

    private val mPref: SharedPreferences
    private val mEditor: SharedPreferences.Editor? = null
    var username: String
        get() = mPref.getString(username, null).toString()
        set(user) {
            val editor = mPref.edit()
            editor.putString(username, user)
            editor.apply()
        }
    var password: String
        get() = mPref.getString(password, null).toString()
        set(pass) {
            val editor = mPref.edit()
            editor.putString(password, pass)
            editor.apply()
        }
    var supported: Boolean
        get() = mPref.getBoolean(supported.toString(), false).toString().toBoolean()
        set(support) {
            val editor = mPref.edit()
            editor.putString(supported.toString(), support.toString())
            editor.apply()
        }

    companion object {
        const val COUNT_VALUE = "count_value"
        const val COUNT_NUM = "count"
        private var sInstance: PreferenceUtil? = null

        fun getInstance(context: Context): PreferenceUtil? {
            if (sInstance == null) sInstance = PreferenceUtil(context.applicationContext)
            return sInstance
        }



    }

    init {
        mPref = PreferenceManager.getDefaultSharedPreferences(context)
    }
}