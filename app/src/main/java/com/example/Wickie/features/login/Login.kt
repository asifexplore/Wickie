package com.example.Wickie.features.login

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class Login(val userName : String, val password : String, val biometric : Boolean, activity: Activity)  {
    val sharedPref : SharedPreferences = activity.getSharedPreferences("biometric", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

}