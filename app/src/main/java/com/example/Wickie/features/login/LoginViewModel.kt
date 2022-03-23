package com.example.Wickie.features.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.example.Wickie.AuthRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.data.RequestAuthCall
import com.example.Wickie.services.NetworkService


class LoginViewModel(private val authRepository: AuthRepository, private val prefRepo: SharedPrefRepo)  : ViewModel() {


    // Live Data for Fingerprint
    //var fingerPrintStatus = MutableLiveData<Boolean>()
    val fingerprintStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    var username = MutableLiveData<String>()

    init{
        fingerprintStatus.value = getFingerprintStatus()
    }

    fun login(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        return authRepository.login(username,password)
    }

    fun getFingerprintStatus() : Boolean
    {
       return prefRepo.getFingerPrintStatus()
    }
    fun getUsername() : String
    {
        return prefRepo.getUsername()
    }
    fun getPassword() : String
    {
        return prefRepo.getPassword()
    }
    fun setUsername(username: String)
    {
        prefRepo.setUsername(username)
    }
    fun setPassword(password: String)
    {
        prefRepo.setPassword(password)
    }
}

class LoginViewModelFactory(private val authRepository : AuthRepository, private val prefRepo: SharedPrefRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository, prefRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}