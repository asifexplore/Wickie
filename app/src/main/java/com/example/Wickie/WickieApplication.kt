package com.example.Wickie

import android.app.Application
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.UserRepository

class WickieApplication: Application() {
    val sharedPrefRepo by lazy { SharedPrefRepo.getInstance(this) }
    val userRepository by lazy { UserRepository.getInstance(this) }
}