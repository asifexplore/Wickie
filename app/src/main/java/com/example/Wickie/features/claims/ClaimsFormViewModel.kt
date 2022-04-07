package com.example.Wickie.features.claims

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.Utils.getCurrentDateTime
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestClaimCall
import com.example.Wickie.features.profile.ProfileViewModel

class ClaimsFormViewModel(val pageType: Int = 0, val claimObj : Claim?, private val prefRepo: SharedPrefRepo ,private val claimRepository: ClaimRepository ) : ViewModel() {
//    private val claimRepository: ClaimRepository = ClaimRepository()
    val pageStatus = MutableLiveData<Int>()
    lateinit var currClaimObj : Claim
    val currPageType = pageType

    init {
        pageStatus.value = 1
        if (pageType == 1)
        {
             //Update
            if (claimObj != null) {
                currClaimObj = claimObj
            }
        }else
        {
            // Create --> Empty Claim, Informmation is updated as user progress thru the form
            currClaimObj = Claim("","","","","","","","","")
        }
    }

    // For ProgressTab
    var descriptionData = arrayOf("Details", "Image", "Done")

    fun incrementPageStatus()
    {
        if (pageStatus.value != 3)
        {
            pageStatus.value = (pageStatus.value)?.plus(1)
        }
    }
    fun decrementPageStatus()
    {
        if (pageStatus.value != 1)
        {
            pageStatus.value = (pageStatus.value)?.minus(1)
        }
    }

    // Create Repo
    // Create Functions
    // title:  String,reason : String,amount : String,type : String, imgUrl : String,claimDate : String
    fun create(username: String = prefRepo.getUsername(), claimObj : Claim = currClaimObj) : MutableLiveData<RequestClaimCall>
    {
        val date = getCurrentDateTime()
        val dateInString = date.toString()
        return claimRepository.create(claimObj.title.toString(),claimObj.reason.toString(),claimObj.amount.toString(),claimObj.type.toString(), claimObj.imageUrl.toString(), dateInString,claimObj.claimDate.toString(), username)
    }
    //title : String,reason : String,amount : String,type : String, imgUrl: String,claimDate : String
    fun update(username: String = prefRepo.getUsername(), claimObj : Claim = currClaimObj)  : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.update(claimObj.title.toString(),claimObj.reason.toString(),claimObj.amount.toString(),claimObj.type.toString(), claimObj.imageUrl.toString(), claimObj.createdDate.toString(),claimObj.claimDate.toString(), claimObj.claimID.toString(), username)
    }

}
class ClaimFormModelFactory(val pageType: String? ,val claimObj: Claim?,private val prefRepo: SharedPrefRepo, private val claimRepository: ClaimRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClaimsFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return pageType?.let { ClaimsFormViewModel(it.toInt(), claimObj,prefRepo, claimRepository) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}