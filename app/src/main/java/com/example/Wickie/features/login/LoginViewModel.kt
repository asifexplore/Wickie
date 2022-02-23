package com.example.Wickie.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Wickie.AuthRepository
import com.example.Wickie.data.source.data.RequestAuthCall

class LoginViewModel( )  : ViewModel() {

    private val authRepository: AuthRepository = AuthRepository()

    private var _loginStatus = MutableLiveData<Boolean>()
    fun loginStatus() : LiveData<Boolean>
    {
        return _loginStatus
    }

    fun login(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        return authRepository.login(username,password)
    }
}