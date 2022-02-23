package com.example.Wickie.features.login

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Wickie.AuthRepository

class LoginViewModal( )  : ViewModel() {

    private val authRepository: AuthRepository = AuthRepository()
    private var _loginStatus = MutableLiveData<Boolean>()
    fun loginStatus() : LiveData<Boolean>
    {
        return _loginStatus
    }

    fun checkLogin(username:String, password: String)
    {
        // Supposed to call Repo
        _loginStatus.value = authRepository.login(username,password)
    }
}