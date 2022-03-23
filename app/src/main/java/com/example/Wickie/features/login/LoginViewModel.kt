package com.example.Wickie.features.login

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.Wickie.AuthRepository
import com.example.Wickie.data.source.data.RequestAuthCall


class LoginViewModel()  : ViewModel() {

    private val authRepository: AuthRepository = AuthRepository()

    // Live Data for Fingerprint
    var fingerPrintStatus = MutableLiveData<Boolean>()
    var username = MutableLiveData<String>()

    fun login(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        return authRepository.login(username,password)
    }
}