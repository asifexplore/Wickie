package com.example.Wickie.features.claims

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestClaimCall
import com.example.Wickie.features.profile.ProfileViewModel

class claimDetailsViewModel(val claimObj : Claim,private val prefRepo: SharedPrefRepo, private val claimRepository: ClaimRepository ) : ViewModel() {

    val claimObject : Claim = claimObj
//    private val claimRepository: ClaimRepository = ClaimRepository()

    // Delete Function for Claims
    fun deleteClaims() : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.delete(claimObject.claimID.toString(), prefRepo.getUsername())
    }

}
class ClaimDetailsModelFactory(val claimObj: Claim?, private val prefRepo: SharedPrefRepo, private val claimRepository: ClaimRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(claimDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return claimObj?.let { claimDetailsViewModel(it, prefRepo, claimRepository) } as T
//            return pageType?.let { ClaimsFormViewModel(it.toInt(), claimObj,prefRepo, claimRepository) } as T
//            return claimObj?.let { claimDetailsViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}