package com.example.Wickie.features.claims

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.data.RequestClaimCall
import com.example.Wickie.Utils.getCurrentDateTime
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.features.profile.ProfileViewModel

class ClaimViewModel(private val prefRepo: SharedPrefRepo, private val claimRepository: ClaimRepository) : ViewModel() {
    private var chosenClaimID = MutableLiveData<Int>()

    // 0 = Adding
    // 1 = Updating
    var pageType = MutableLiveData<Int>()

    // Claim Form Status
    var descriptionData = arrayOf("Details", "Image", "Done")
    val pageStatus = MutableLiveData<Int>()

    init {
        pageStatus.value = 1
        pageType.value = 0 // Hardcoded to 0 for testing Update Feature
        chosenClaimID.value = -1
    }

    fun retrieve(username : String = prefRepo.getUsername()) : MutableLiveData<RequestClaimCall>
    {
//        return claimRepository.retrieve(prefRepo.getUsername())
        return claimRepository.retrieve(username)
    }

    fun setChosenClaim(num : Int)
    {
        chosenClaimID.value = num
    }

    fun getChosenClaimID(): Int? {
        return chosenClaimID.value
    }
}
class ClaimFragModelFactory(private val prefRepo: SharedPrefRepo, private val claimRepository: ClaimRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClaimViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClaimViewModel(prefRepo, claimRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}