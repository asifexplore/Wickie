package com.example.Wickie.features.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.Wickie.AuthRepository
import com.example.Wickie.data.source.UserRepository
import com.example.Wickie.data.source.data.RequestAuthCall

class ProfileViewModel( )  : ViewModel() {

    private val userRepository: UserRepository = UserRepository()

    fun retrieve(username:String) : MutableLiveData<RequestAuthCall>
    {
        return userRepository.retrieve(username)
    }
}