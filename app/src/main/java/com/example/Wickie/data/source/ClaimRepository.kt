package com.example.Wickie.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestClaimCall
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ClaimRepository {
    val requestCall = RequestClaimCall()

    // Create
    fun create(date : String,type : String,reason : String,amount : String) : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
        // Add onto Firebase
        val claim = Claim()
        claim.date = date
        claim.type = type
        claim.reason = reason
        claim.amount = amount

        database.child("asif").child("3").setValue(claim).addOnSuccessListener {
            requestCall.status = 2
            requestCall.message = "Add Success"
            mLiveData.postValue(requestCall)
        }.addOnFailureListener(){
            Log.d("ClaimRepo", "Failed")
            requestCall.status = 1
            requestCall.message = "Add Failed"
            mLiveData.postValue(requestCall)
        }

        return mLiveData
    }

}