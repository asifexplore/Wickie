package com.example.Wickie.data.source

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.data.source.data.RequestClaimCall
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


/*
    This Repository contains functions to Create, Read, Update, Delete Claims.
*/
class ClaimRepository {
    val requestCall = RequestClaimCall()

    /*
        Function Name: create
        Function Signature: (title : String, reason : String, amount : String, type : String, imageUrl: String, createdDate : String, claimDate : String, userID : String )
        Function Purpose: To update Claim request.
        Function Arguments:
                title = Title of the Claim Request
                reason = reason for requesting claim
                amount = cost of claim
                type = Type of Claim | 3 options = phone, meal, transport
                imageUrl = Firebase Storage URL
                createdDate = Auto generated Date of the day user applies for claim
                claimDate = Date of purchase
                userID  = String | Refers to the ID of the User
        Function Return : MutableLiveData of RequestClaimCall() Object
        Fields inside RequestClaimCall Object:
                var status = 0
                var message : String = "No Message"
                var claimDetail: Claim?  = null
                var claimArray : ArrayList<Claim> = ArrayList<Claim>()
                var claimTotal : Double? = null
    */
    fun create(title : String, reason : String, amount : String, type : String, imageUrl: String, createdDate : String, claimDate : String, userID : String) :
            MutableLiveData<RequestClaimCall>
    {
        val status = "Pending"
        val mLiveData = MutableLiveData<RequestClaimCall>()
        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"

        val database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        var claimID : String = ""

        database.get().addOnSuccessListener {
            if (it.exists() && it.child("users").child(userID).exists())
            {
                claimID = it.child("users").child(userID).child("no_of_claims").value.toString()
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
                    requestCall.status = 1
                    requestCall.message = "Add Failed"
                    mLiveData.postValue(requestCall)
                }
            }else
            {
                //user does not exist
            }
        }
        return mLiveData
    }
    /*
        Function Name: update
        Function Signature: (title : String, reason : String, amount : String, type : String, imageUrl: String, createdDate : String, claimDate : String, claimID : String, userID : String )
        Function Purpose: To update Claim request.
        Function Arguments:
                title = Title of the Claim Request
                reason = reason for requesting claim
                amount = cost of claim
                type = Type of Claim | 3 options = phone, meal, transport
                imageUrl = Firebase Storage URL
                createdDate = Auto generated Date of the day user applies for claim
                claimDate = Date of purchase
                claimID = String | ID of the specific claim
                userID  = String | Refers to the ID of the User
        Function Return : MutableLiveData of RequestClaimCall Object
        Fields inside RequestClaimCall Object:
                var status = 0
                var message : String = "No Message"
                var claimDetail: Claim?  = null
                var claimArray : ArrayList<Claim> = ArrayList<Claim>()
                var claimTotal : Double? = null
    */
    fun update(title : String, reason : String, amount : String, type : String, imageUrl: String, createdDate : String, claimDate : String, claimID : String, userID : String ) : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"

        val database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
        // Add onto Firebase
        val claim = Claim(title, reason, amount, "Pending" ,type,imageUrl, createdDate,claimDate, claimID)

        database.child(userID).child(claimID).setValue(claim).addOnSuccessListener {
            requestCall.status = 2
            requestCall.message = "Update Success"
            mLiveData.postValue(requestCall)
        }.addOnFailureListener(){
            requestCall.status = 1
            requestCall.message = "Update Failed"
            mLiveData.postValue(requestCall)
        }
        return mLiveData
    }
    /*
        Function Name: delete
        Function Signature: (claimID : String, userID : String)
        Function Purpose: To delete Claim request.
        Function Arguments:
                claimID = String | ID of the specific claim
                userID  = String | Refers to the ID of the User
        Function Return : MutableLiveData of RequestClaimCall Object
        Fields inside RequestClaimCall Object:
                var status = 0
                var message : String = "No Message"
                var claimDetail: Claim?  = null
                var claimArray : ArrayList<Claim> = ArrayList<Claim>()
                var claimTotal : Double? = null
    */
    fun delete(claimID : String, userID : String) : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        // In Progress
        requestCall.status = 0
        requestCall.message = "Fetching Data"

        val database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
        database.child(userID).child(claimID).child("deleted").setValue("true").addOnSuccessListener {
            requestCall.status = 2
            requestCall.message = "Delete Success"
            mLiveData.postValue(requestCall)
        }.addOnFailureListener(){
            requestCall.status = 1
            requestCall.message = "Delete Failed"
            mLiveData.postValue(requestCall)
        }
        return mLiveData
    }
    /*
        Function Name: retrieve
        Function Signature: (userID : String)
        Function Purpose: To retrieve all Claim requests made by user which has not been deleted. Sorts return data to latest first.
        Function Arguments:
                userID  = String | Refers to the ID of the User
        Function Return : MutableLiveData of RequestClaimCall Object
        Fields inside RequestClaimCall Object:
                var status = 0
                var message : String = "No Message"
                var claimDetail: Claim?  = null
                var claimArray : ArrayList<Claim> = ArrayList<Claim>()
                var claimTotal : Double? = null
    */
    fun retrieve(userID :String) : MutableLiveData<RequestClaimCall>
    {
        val mLiveData = MutableLiveData<RequestClaimCall>()
        val requestCall = RequestClaimCall()
        var claimTotal : Double = 0.0

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"

        val database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("claims")
        requestCall.message = "After DB Ref"
        database.get().addOnSuccessListener {
        requestCall.message = "Inside SuccessListener"
            if (it.exists())
            {
                requestCall.message = "Inside Exist"
                val claims = it.child(userID).value as ArrayList<HashMap<String,Any?>>

                val claimList = arrayListOf<Claim>()
                for (i in claims)
                {
                    if (i["deleted"].toString() != "true")
                    {
                        val l = Claim(i["title"].toString(), i["reason"].toString(), i["amount"].toString(), i["status"].toString(),
                            i["type"].toString(), i["imageUrl"].toString(), i["createdDate"].toString(), i["claimDate"].toString(),i["claimID"].toString())
                        claimList.add(l)
                        val x: String = i["amount"].toString()
                        if (i["status"].toString() != "Rejected")
                        {
                            claimTotal = (x.toDouble() +  claimTotal.toDouble())
                        }
                    }
                    else{
                        // To not display deleted claims
                    }
                }
                requestCall.message = "Before DataLoop"
                // Swapping ArrayList
                val swapArrayList = arrayListOf<Claim>()

                // Total in Array
                var totalNum = claimList.count()
                // To prevent index out of range error
                totalNum -= 1

                for (n in totalNum downTo 0)
                {
                    swapArrayList.add(claimList[n])
                }
                requestCall.status = 2
                requestCall.message = "DATA FOUND"
                requestCall.claimTotal = (10000.0 - claimTotal)
                requestCall.claimArray = swapArrayList
            }else{
                // Data does not exits
                requestCall.status = 1
                requestCall.message = "NO DATA FOUND"
            }
            mLiveData.postValue(requestCall)

        }.addOnFailureListener()
        {
            requestCall.message = "Inside Failure Listener"
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