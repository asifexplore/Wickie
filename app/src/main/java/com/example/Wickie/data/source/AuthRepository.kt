package com.example.Wickie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.RequestCall
import com.example.Wickie.data.source.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AuthRepository {

    fun login(email:String, password:String): Boolean {
        // Supposed to Retrieve Data

        if (password == "t")
        {
            return false
        }
        return true
    }

    fun select() : MutableLiveData<RequestCall>
    {
        val mLiveData = MutableLiveData<RequestCall>()
        val requestCall = RequestCall()
        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var MEM_CACHE: ArrayList<User> = ArrayList()

        val database = Firebase.database
        val myRef = database.getReference("users")
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // Clear Data away from previous call
                MEM_CACHE.clear()
                if (snapshot.exists() && snapshot.childrenCount > 0)
                {
                   for (ds in snapshot.children)
                   {
                        val user = ds.getValue(User::class.java)
                       if (user != null )
                       {

                       }
                   }
                    requestCall.status = 1
                    requestCall.message = "DATA FOUND"
                }else {
                    requestCall.status = 1
                    requestCall.message = "NO DATA FOUND"
                }
                requestCall.users = MEM_CACHE
                mLiveData.postValue(requestCall)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("AuthRepo", "Failed to read value.", error.toException())
            }

        })
        return mLiveData
    }


}