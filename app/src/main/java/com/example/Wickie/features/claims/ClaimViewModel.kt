package com.example.Wickie.features.claims

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.data.RequestClaimCall


class ClaimViewModel : ViewModel() {
    private val claimRepository: ClaimRepository = ClaimRepository()
    // Create Repo
    // Create Functions
    fun create(date : String,type : String,reason : String,amount : String)  : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.create(date,type,reason,amount)
    }
}