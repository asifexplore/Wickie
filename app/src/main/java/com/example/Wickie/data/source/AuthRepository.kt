package com.example.Wickie

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.RequestAuthCall
import com.example.Wickie.data.source.data.User
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class AuthRepository {

    fun login(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        val mLiveData = MutableLiveData<RequestAuthCall>()
        val requestCall = RequestAuthCall()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        val database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")

        database.get().addOnSuccessListener {
            Log.d("AuthRepo",it.toString())
            if (it.exists() && it.child(username).exists() )
            {
                Log.d("AuthRepo","Inside exists")
                Log.d("AuthRepo",it.child(username).child("user_pw").value.toString())
                if (password.equals(it.child(username).child("user_pw").value))
                {
                    requestCall.status = 2
                    requestCall.message = "DATA FOUND"
                    val user  = User()
                    user.user_email = it.child(username).child("user_email").toString()
                    requestCall.userDetail = user
                }else
                {
                    requestCall.status = 1
                    requestCall.message = "NO DATA FOUND"
                }
            }else{
                Log.d("AuthRepo",it.child(username).toString())
                // Data does not exits
                requestCall.status = 1
                requestCall.message = "NO DATA FOUND"
                Log.d("AuthRepo", "user does not exist ")
            }


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

    companion object {
        // The usual for debugging
        private val TAG: String = "AuthPreferencesRepository"

        // Boilerplate-y code for singleton: the private reference to this self
        @Volatile
        private var INSTANCE: AuthRepository? = null

        fun getInstance(context: Context): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = AuthRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}