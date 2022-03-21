package com.example.Wickie.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.Wickie.AuthRepository
import com.example.Wickie.data.source.data.RequestAuthCall

class LoginViewModel( )  : ViewModel() {

    private val authRepository: AuthRepository = AuthRepository()

    // Live Data for Fingerprint
    var fingerPrintStatus = MutableLiveData<Boolean>()
    // Function to change Fingerprint Status

    init {
        fingerPrintStatus.value = false
    }

    fun login(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        return authRepository.login(username,password)
    }

    fun disableFingerprint()
    {

    }
    fun enableFingerprint()
    {

    }

}