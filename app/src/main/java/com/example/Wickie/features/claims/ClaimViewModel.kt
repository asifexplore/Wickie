package com.example.Wickie.features.claims

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.data.RequestClaimCall
import com.example.Wickie.Utils.getCurrentDateTime

class ClaimViewModel : ViewModel() {

    private val claimRepository: ClaimRepository = ClaimRepository()

    // Claim Form Status
    var descriptionData = arrayOf("Details", "Image", "Done")
    val pageStatus = MutableLiveData<Int>()

    init {
        pageStatus.value = 1
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

//    fun update(date : String,type : String,reason : String,amount : String, id: String)  : MutableLiveData<RequestClaimCall>
//    {
//        return claimRepository.update(date,type,reason,amount, id)
//    }
}