package com.example.Wickie.features.login

import androidx.lifecycle.*
import com.example.Wickie.AuthRepository
import com.example.Wickie.data.source.data.RequestAuthCall
import com.example.Wickie.data.source.data.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel( )  : ViewModel() {

    private val authRepository: AuthRepository = AuthRepository()

    fun login(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        return authRepository.login(username,password)
    }
}