package com.example.Wickie.features.claims

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.data.RequestClaimCall


class ClaimViewModel : ViewModel() {

    private val claimRepository: ClaimRepository = ClaimRepository()

    // Claim Form Status
    var descriptionData = arrayOf("Details", "Image", "Done")
    val pageStatus = MutableLiveData<Int>()

    init {
        pageStatus.value = 0
    }

    fun incrementPageStatus()
    {
        if (pageStatus.value != 2)
        {
            pageStatus.value = (pageStatus.value)?.plus(1)
        }
    }
    fun decrementPageStatus()
    {
        if (pageStatus.value != 0)
        {
            pageStatus.value = (pageStatus.value)?.minus(1)
        }
    }

    // Create Repo
    // Create Functions
    fun create(date : String,type : String,reason : String,amount : String)  : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.create(date,type,reason,amount)
    }

    fun update(date : String,type : String,reason : String,amount : String, id: String)  : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.update(date,type,reason,amount, id)
    }
}