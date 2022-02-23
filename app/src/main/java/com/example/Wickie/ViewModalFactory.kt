package com.example.Wickie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.features.login.LoginViewModel
import java.lang.IllegalArgumentException

class ViewModalFactory private constructor(
    // Should be base repository
    // https://www.youtube.com/watch?v=Yk97UIVT0yk&list=PLk7v1Z2rk4hgmIvyw8rvpiEQxIAbJvDAF&index=5
    private val repository: AuthRepository

) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel() as T
            else -> throw IllegalArgumentException("ViewModalClass not Found ")
        }
    }

}