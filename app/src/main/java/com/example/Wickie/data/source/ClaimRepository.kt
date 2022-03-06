package com.example.Wickie.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestAuthCall
import com.example.Wickie.data.source.data.RequestClaimCall
import com.google.firebase.database.*

class ClaimRepository {
    val requestCall = RequestClaimCall()

    // Create
//    fun create(date : String,type : String,reason : String,amount : String) : MutableLiveData<RequestClaimCall>
//    {
//        val mLiveData = MutableLiveData<RequestClaimCall>()
//        // In Progress
//        requestCall.status = 1
//        requestCall.message = "Fetching Data"
//        mLiveData.value = requestCall
//
//        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
//        // Add onto Firebase
//        val claim = Claim()
//        claim.date = date
//        claim.type = type
//        claim.reason = reason
//        claim.amount = amount
//
//        database.child("asif").child("4").setValue(claim).addOnSuccessListener {
//            requestCall.status = 2
//            requestCall.message = "Add Success"
//            mLiveData.postValue(requestCall)
//        }.addOnFailureListener(){
//            Log.d("ClaimRepo", "Failed")
//            requestCall.status = 1
//            requestCall.message = "Add Failed"
//            mLiveData.postValue(requestCall)
//        }
//
//        return mLiveData
//    }
//
//    fun update(date : String,type : String,reason : String,amount : String, id: String) : MutableLiveData<RequestClaimCall>
//    {
//        val mLiveData = MutableLiveData<RequestClaimCall>()
//        // In Progress
//        requestCall.status = 1
//        requestCall.message = "Fetching Data"
//        mLiveData.value = requestCall
//
//        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
//        // Add onto Firebase
//        val claim = Claim()
//        claim.claimDate = date
//        claim.type = type
//        claim.reason = reason
//        claim.amount = amount
//
//
//
//
//        database.child("asif").child(id).setValue(claim).addOnSuccessListener {
//            requestCall.status = 2
//            requestCall.message = "Add Success"
//            mLiveData.postValue(requestCall)
//        }.addOnFailureListener(){
//            Log.d("ClaimRepo", "Failed")
//            requestCall.status = 1
//            requestCall.message = "Add Failed"
//            mLiveData.postValue(requestCall)
//        }
//
//        return mLiveData
//    }
/* */
    fun retrieve() : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        val requestCall = RequestClaimCall()
        var claimTotal : Int = 0

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")

        database.get().addOnSuccessListener {
            Log.d("ClaimRepo",it.toString())
            if (it.exists())
            {
                Log.d("ClaimRepo","Inside exists")
                Log.d("ClaimRepo",it.toString())
                Log.d("ClaimRepo", it.child("asif").toString())

                Log.d("ClaimsRepos", it.child("asif").value.toString())
                val claims = it.child("asif").value as ArrayList<HashMap<String,Any?>>

                val claimList = arrayListOf<Claim>()
                for (i in claims)
                {
                    Log.d("ClaimRepos","Inside Loop")
                    // var title: String?, var reason : String? ,var amount : String? , var status:String?, var type : String?,
                    //            var imgUrl : String?, var createdDate : String?, var claimDate : String?
                    val l = Claim(i["title"].toString(), i["reason"].toString(), i["amount"].toString(), i["status"].toString(),
                        i["type"].toString(), i["imgUrl"].toString(), i["createdDate"].toString(), i["claimDate"].toString())
                    Log.d("ClaimRepos",l.type.toString())
                    claimList.add(l)
                    val x: String = i["amount"].toString()
                    claimTotal = (x.toInt() +  claimTotal.toInt())
                }
                Log.d("ClaimRepo", claimList.toString())
                requestCall.status = 2
                requestCall.message = " DATA FOUND"
                requestCall.claimTotal = (10000 - claimTotal)
                requestCall.claimArray = claimList

            }else{
//                Log.d("ClaimsRepo",it.child(username).toString())
                // Data does not exits
                requestCall.status = 1
                requestCall.message = "NO DATA FOUND"
                Log.d("ClaimRepo", "user does not exist ")
            }

            mLiveData.postValue(requestCall)

        }.addOnFailureListener()
        {
            Log.d("ClaimRepo", "Failed")
            requestCall.status = 1
            requestCall.message = "NO DATA FOUND"
            mLiveData.postValue(requestCall)
        }
        return mLiveData
    }


}