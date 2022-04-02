package com.example.Wickie.data.source

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestClaimCall
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ClaimRepository {
    val requestCall = RequestClaimCall()

    // Create
//    var title: String?, var reason : String? ,var amount : String? , var status:String?, var type : String?,
//    var imgUrl : String?, var createdDate : String?, var claimDate : String?
    fun create(title : String, reason : String, amount : String, type : String, imageUrl: String, createdDate : String, claimDate : String, userID : String) :
            MutableLiveData<RequestClaimCall>
    {
        val status = "Pending"
        val mLiveData = MutableLiveData<RequestClaimCall>()
//        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        var claimID : String = ""

        database.get().addOnSuccessListener {
            Log.d("ClaimRepoID",it.toString())

            if (it.exists() && it.child("users").child(userID).exists())
            {
                Log.d("ClaimRepoID",it.child("users").child(userID).toString())
                claimID = it.child("users").child(userID).child("no_of_claims").value.toString()
                Log.d("ClaimRepoNew",claimID)
                // Add onto Firebase
                val claim = Claim(title, reason, amount, status,type,imageUrl, createdDate,claimDate, claimID.toString())
                database.child("claims").child(userID).child(claimID.toString()).setValue(claim).addOnSuccessListener {
                    var newClaimID : Int = claimID.toInt()
                    newClaimID += 1
                    //  Changing no_of_claims under users - "username" ==> Important to prevent overriding of existing claim request
                    database.child("users").child(userID).child("no_of_claims").setValue(newClaimID).addOnSuccessListener {
                        // Sucessfully added
                    }

                    requestCall.status = 2
                    requestCall.message = "Add Success"
                    mLiveData.postValue(requestCall)
                }.addOnFailureListener(){
                    Log.d("ClaimRepo", "Failed")
                    requestCall.status = 1
                    requestCall.message = "Add Failed"
                    mLiveData.postValue(requestCall)
                }

                // Adding to User - no_of_claims (Stores total number of claims)

            }else
            {
                Log.d("ClaimRepoID","Inside Else")
            }
        }
        return mLiveData
    }
    //    var title: String?, var reason : String? ,var amount : String? , var status:String?, var type : String?,
//    var imgUrl : String?, var createdDate : String?, var claimDate : String?
    fun update(title : String, reason : String, amount : String, type : String, imageUrl: String, createdDate : String, claimDate : String, claimID : String, userID : String ) : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
        // Add onto Firebase
        val claim = Claim(title, reason, amount, "Pending" ,type,imageUrl, createdDate,claimDate, claimID)

        database.child(userID).child(claimID).setValue(claim).addOnSuccessListener {
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
    fun delete(claimID : String, userID : String) : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        // In Progress
        requestCall.status = 0
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
        database.child(userID).child(claimID).child("deleted").setValue("true").addOnSuccessListener {
            requestCall.status = 2
            requestCall.message = "Delete Success"
            mLiveData.postValue(requestCall)
        }.addOnFailureListener(){
            Log.d("ClaimRepo", "Failed")
            requestCall.status = 1
            requestCall.message = "Delete Failed"
            mLiveData.postValue(requestCall)
        }
        return mLiveData
    }

    fun retrieve(userID :String) : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        val requestCall = RequestClaimCall()
        var claimTotal : Double = 0.0

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
                Log.d("ClaimRepo", it.child(userID).toString())

                Log.d("ClaimsRepos", it.child(userID).value.toString())
                val claims = it.child(userID).value as ArrayList<HashMap<String,Any?>>

                val claimList = arrayListOf<Claim>()
                for (i in claims)
                {
                    if (i["deleted"].toString() != "true")
                    {
                        Log.d("ClaimRepos","Inside Loop")
                        // var title: String?, var reason : String? ,var amount : String? , var status:String?, var type : String?,
                        //            var imgUrl : String?, var createdDate : String?, var claimDate : String?
                        val l = Claim(i["title"].toString(), i["reason"].toString(), i["amount"].toString(), i["status"].toString(),
                            i["type"].toString(), i["imageUrl"].toString(), i["createdDate"].toString(), i["claimDate"].toString(),i["claimID"].toString())
                        Log.d("ClaimRepos",l.type.toString())
                        claimList.add(l)
                        val x: String = i["amount"].toString()
                        claimTotal = (x.toDouble() +  claimTotal.toDouble())
                    }
                    else{
                        // To not display deleted claims
                    }

                }

                // Swapping ArrayList
                var swapArrayList = arrayListOf<Claim>()

                // Total in Array
                var totalNum = claimList.count()
                // To prevent index out of range error
                totalNum -= 1

                for (n in totalNum downTo 0)
                {
                    swapArrayList.add(claimList.get(n))
                }

                Log.d("ClaimRepo", claimList.toString())
                requestCall.status = 2
                requestCall.message = " DATA FOUND"
                requestCall.claimTotal = (10000.0 - claimTotal)
                requestCall.claimArray = swapArrayList

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
    companion object {
        // The usual for debugging
        private val TAG: String = "ClaimRepository"

        // Boilerplate-y code for singleton: the private reference to this self
        @Volatile
        private var INSTANCE: ClaimRepository? = null

        fun getInstance(context: Context): ClaimRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = ClaimRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}