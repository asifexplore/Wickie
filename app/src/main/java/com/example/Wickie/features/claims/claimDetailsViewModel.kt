package com.example.Wickie.features.claims

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestClaimCall

class claimDetailsViewModel(val claimObj : Claim) : ViewModel() {

    val claimObject : Claim = claimObj
    private val claimRepository: ClaimRepository = ClaimRepository()

    // Delete Function for Claims
    fun deleteClaims() : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.delete(claimObject.claimID.toString())
    }

}
class ClaimDetailsModelFactory(val claimObj: Claim?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(claimDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return claimObj?.let { claimDetailsViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}