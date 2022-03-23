package com.example.Wickie.features.claims

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.Utils.getCurrentDateTime
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestClaimCall

class ClaimsFormViewModel(val pageType: Int = 0, val claimObj : Claim?) : ViewModel() {
    private val claimRepository: ClaimRepository = ClaimRepository()
    val pageStatus = MutableLiveData<Int>()
    lateinit var currClaimObj : Claim
    val currPageType = pageType
//    val latestClaimID : MutableLiveData<String> = claimRepository.retrieveLatestID().observe()
//
//    fun retrieveLatestID() : MutableLiveData<String>
//    {
//        latestClaimID.value = claimRepository.retrieveLatestID().value
//        return latestClaimID
//    }

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
//        Log.d("ClaimsFormViewModel",latestClaimID.value.toString())
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
    fun create() : MutableLiveData<RequestClaimCall>
    {
        val date = getCurrentDateTime()
        val dateInString = date.toString()
        return claimRepository.create(currClaimObj.title.toString(),currClaimObj.reason.toString(),currClaimObj.amount.toString(),currClaimObj.type.toString(), currClaimObj.imageUrl.toString(), dateInString,currClaimObj.claimDate.toString())
    }
    //title : String,reason : String,amount : String,type : String, imgUrl: String,claimDate : String
    fun update()  : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.update(currClaimObj.title.toString(),currClaimObj.reason.toString(),currClaimObj.amount.toString(),currClaimObj.type.toString(), currClaimObj.imageUrl.toString(), currClaimObj.createdDate.toString(),currClaimObj.claimDate.toString(), currClaimObj.claimID.toString())
    }

}
class ClaimFormModelFactory(val pageType: String? ,val claimObj: Claim?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClaimsFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")

            return claimObj?.let { pageType?.let { it1 -> ClaimsFormViewModel(it1.toInt(), it) } } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}