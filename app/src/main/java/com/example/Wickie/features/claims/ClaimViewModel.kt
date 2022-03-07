package com.example.Wickie.features.claims

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.data.RequestClaimCall
import com.example.Wickie.Utils.getCurrentDateTime
import com.example.Wickie.data.source.data.Claim

class ClaimViewModel : ViewModel() {

    private val claimRepository: ClaimRepository = ClaimRepository()

    // 0 = Adding
    // 1 = Updating
    var pageType = MutableLiveData<Int>()

    // Set claim object when user clicks on claim from recyclerView
    val chosenClaim : Claim  = Claim("tst4", "stst" ,"123" , "Pending", "Transport",
    "2022_02_24_08_26_21", "Mon Mar 07 07:13:43 GMT 2022", "Choose Date", "16")

    // Claim Form Status
    var descriptionData = arrayOf("Details", "Image", "Done")
    val pageStatus = MutableLiveData<Int>()

    init {
        pageStatus.value = 1
        pageType.value = 1 // Hardcoded to 0 for testing Update Feature
    }

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
    fun create(title:  String,reason : String,amount : String,type : String, imgUrl : String,claimDate : String)  :
            MutableLiveData<RequestClaimCall>
    {
        val date = getCurrentDateTime()
        val dateInString = date.toString()
        return claimRepository.create(title,reason,amount,type, imgUrl, dateInString,claimDate)
    }

    fun update(title : String,reason : String,amount : String,type : String, imgUrl: String,claimDate : String)  : MutableLiveData<RequestClaimCall>
    {
        return claimRepository.update(title,reason,amount,type, imgUrl, chosenClaim.createdDate.toString(),claimDate, chosenClaim.claimID.toString())
    }
}