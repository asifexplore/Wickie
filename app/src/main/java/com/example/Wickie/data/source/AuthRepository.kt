package com.example.Wickie

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.RequestAuthCall
import com.example.Wickie.data.source.data.User
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class AuthRepository {
    /*
         Function Name: login
         Function Signature: (username:String, password: String)
         Function Purpose: To check if entered credentials tally with database.
         Function Arguments:
                 username = String | Username the user inputted.
                 password  = String | Password the user inputted.
         Function Return : MutableLiveData of RequestAuthCall Object
         Fields inside RequestAuthCall Object:
                var status = 0
                var message : String = "No Message"
                var userDetail: User = User()
         Function Success :
                message = DATA FOUND when username and password is correct.
                message = NO DATA FOUND when anything goes wrong.
     */
    fun login(username:String, password: String) : MutableLiveData<RequestAuthCall>
    {
        val mLiveData = MutableLiveData<RequestAuthCall>()
        val requestCall = RequestAuthCall()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"

        val database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")

        database.get().addOnSuccessListener {
            if (it.exists() && it.child(username).exists() )
            {
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
                // Data does not exits
                requestCall.status = 1
                requestCall.message = "NO DATA FOUND"
            }
            mLiveData.postValue(requestCall)

        }.addOnFailureListener()
        {
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