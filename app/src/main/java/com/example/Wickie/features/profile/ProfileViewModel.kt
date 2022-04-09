package com.example.Wickie.features.profile

import androidx.lifecycle.*
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.UserRepository
import com.example.Wickie.data.source.data.RequestAuthCall

class ProfileViewModel(private val userRepository : UserRepository, private val prefRepo: SharedPrefRepo)  : ViewModel() {

    fun retrieve(username:String) : MutableLiveData<RequestAuthCall>
    {
        return userRepository.retrieve(username)
    }

    fun setFingerPrintStatus(status: Boolean)
    {
        prefRepo.setFingerPrintStatus(status)
    }
    fun getFingerPrintStatus() : Boolean
    {
        return prefRepo.getFingerPrintStatus()
    }

    fun logout()
    {
        prefRepo.clearData()
    }

    fun getUsername() : String
    {
        return prefRepo.getUsername()
    }



}
class ProfileViewModelFactory(private val userRepository : UserRepository, private val prefRepo: SharedPrefRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository, prefRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}