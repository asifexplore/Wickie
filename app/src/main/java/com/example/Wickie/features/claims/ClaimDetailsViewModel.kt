package com.example.Wickie.features.claims

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestClaimCall

/*
*  ClaimDetailsViewModel contains the function to Delete Claims
*
* Functions Within:
* ==========================================================================
* Function Name: deleteClaims
* Function Purpose: Delete Selected Claims
* Function Signature: (claimObj : Claim = claimObject, username : String = prefRepo.getUsername())
* Function Arguments:
*          claimObj = Claim Object that was selected
*           username = username of user -> has a default value which is obtained from Shared Preference.
* Results:
*         Success: Deletes Claim
*         Failed:
*---------------------------------------------------
*/
class ClaimDetailsViewModel(val claimObj : Claim, private val prefRepo: SharedPrefRepo, private val claimRepository: ClaimRepository ) : ViewModel() {

    private val claimObject : Claim = claimObj
    // Delete Function for Claims. Triggers and Return MutableLiveData from Delete Function inside Claim Repo.
    fun deleteClaims(claimObj : Claim = claimObject, username : String = prefRepo.getUsername()) : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.delete(claimObj.claimID.toString(), username)
    }
}
class ClaimDetailsModelFactory(private val claimObj: Claim?, private val prefRepo: SharedPrefRepo, private val claimRepository: ClaimRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClaimDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return claimObj?.let { ClaimDetailsViewModel(it, prefRepo, claimRepository) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}