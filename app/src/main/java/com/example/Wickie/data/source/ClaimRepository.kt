package com.example.Wickie.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestAuthCall
import com.example.Wickie.data.source.data.RequestClaimCall
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ClaimRepository {
    val requestCall = RequestClaimCall()

    // Create
//    var title: String?, var reason : String? ,var amount : String? , var status:String?, var type : String?,
//    var imgUrl : String?, var createdDate : String?, var claimDate : String?
    fun create(title : String,reason : String,amount : String,type : String, imgUrl: String, createdDate : String,claimDate : String ) :
            MutableLiveData<RequestClaimCall>
    {
        val status = "Pending"
        val mLiveData = MutableLiveData<RequestClaimCall>()
        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var claimID = "16"

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
        // Add onto Firebase
        val claim = Claim(title, reason, amount, status,type,imgUrl, createdDate,claimDate, claimID)

        database.child("asif").child(claimID).setValue(claim).addOnSuccessListener {
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
    //    var title: String?, var reason : String? ,var amount : String? , var status:String?, var type : String?,
//    var imgUrl : String?, var createdDate : String?, var claimDate : String?
    fun update(title : String,reason : String,amount : String,type : String, imgUrl: String, createdDate : String,claimDate : String, claimID : String ) : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
        // Add onto Firebase
        val claim = Claim(title, reason, amount, "Pending" ,type,imgUrl, createdDate,claimDate, claimID)

        database.child("asif").child(claimID).setValue(claim).addOnSuccessListener {
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
/*
    fun retrieve(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        val requestCall = RequestClaimCall()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")

        database.get().addOnSuccessListener {
//            Log.d("ClaimRepo",it.toString())
            if (it.exists() && it.child(username).exists() )
            {
//                Log.d("AuthRepo","Inside exists")
//                Log.d("AuthRepo",it.child(username).child("user_pw").value.toString())
                requestCall.status = 2
                requestCall.message = " DATA FOUND"
                requestCall.userDetail = it.child(username)
            }else{
//                Log.d("ClaimsRepo",it.child(username).toString())
                // Data does not exits
                requestCall.status = 1
                requestCall.message = "NO DATA FOUND"
//                Log.d("AuthRepo", "user does not exist ")
            }

            requestCall.userDetail
            mLiveData.postValue(requestCall)

        }.addOnFailureListener()
        {
//            Log.d("AuthRepo", "Failed")
            requestCall.status = 1
            requestCall.message = "NO DATA FOUND"
            mLiveData.postValue(requestCall)
        }
        return mLiveData
    }
*/
}