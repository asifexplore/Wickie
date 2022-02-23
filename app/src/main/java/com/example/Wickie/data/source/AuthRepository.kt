package com.example.Wickie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.RequestAuthCall
import com.google.firebase.database.*

class AuthRepository {

    fun login(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        val mLiveData = MutableLiveData<RequestAuthCall>()
        val requestCall = RequestAuthCall()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
        database.get().addOnSuccessListener {
            if (it.exists() && it.child("users").child(username).exists() )
            {
                // Need to remove stuff from object
                Log.d("LoginActivity",it.child("users").child(username).child("user_pw").toString())
                Log.d("LoginActivity",password)
                    if (password.equals(it.child("users").child(username).child("user_pw").value))
                    {
                        requestCall.status = 2
                        requestCall.message = "DATA FOUND"
                    }else
                    {
                        requestCall.status = 1
                        requestCall.message = "NO DATA FOUND"
                    }
            }else{
                // Data does not exits
                requestCall.status = 1
                requestCall.message = "NO DATA FOUND"
                Log.d("AuthRepo", "user does not exist ")
            }

            requestCall.userDetail
            mLiveData.postValue(requestCall)

        }.addOnFailureListener()
        {
            Log.d("AuthRepo", "Failed")
            requestCall.status = 1
            requestCall.message = "NO DATA FOUND"
            mLiveData.postValue(requestCall)
        }
        return mLiveData
    }
}