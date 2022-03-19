package com.example.Wickie.hardware

import androidx.lifecycle.*
import com.example.Wickie.AuthRepository
import com.example.Wickie.data.source.data.RequestAuthCall
import com.example.Wickie.data.source.data.UserPreferencesRepository
import com.example.Wickie.features.login.Login
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BiometricViewModel (private val dataStore: UserPreferencesRepository) : ViewModel()  {
    private val authRepository: AuthRepository = AuthRepository()

    val readUsername = dataStore.readUsername.asLiveData()
    val readPassword = dataStore.readPassword.asLiveData()
    val readBiometric = dataStore.readBiometric.asLiveData()

    fun saveToDataStore(login: Login) = viewModelScope.launch (Dispatchers.IO){
        dataStore.saveLogin(login)
    }

    fun biometricLogin() {
        val supported = readBiometric.value

        if (supported == true) {
            login()
        }

    }

    fun login() : MutableLiveData<RequestAuthCall>
    {
        return authRepository.login(readUsername.toString(), readPassword.toString())

    }

}

class BiometricViewModelFactory(private val dataStore: UserPreferencesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BiometricViewModel::class.java)) {
            return BiometricViewModel(dataStore) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}