package com.example.Wickie.data.source

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.RequestAuthCall
import com.example.Wickie.data.source.data.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepository {

    //Retrieve User for User Profile
    fun retrieve(username:String): MutableLiveData<RequestAuthCall>
    {
        val mLiveData = MutableLiveData<RequestAuthCall>()
        val requestCall = RequestAuthCall()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"


        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")

        database.get().addOnSuccessListener {
            Log.d("AuthRepo",it.toString())
            if (it.exists() && it.child(username).exists() )
            {
                Log.d("AuthRepo","Inside exists")
                Log.d("AuthRepo",it.child(username).child("user_pw").value.toString())
                requestCall.status = 2
                requestCall.message = "DATA FOUND"
                var user  = User()
                user.user_email = it.child(username).child("user_email").value.toString()
                user.user_name = it.child(username).value.toString()
                user.user_dob = it.child(username).child("user_dob").value.toString()
                user.user_position = it.child(username).child("user_position").value.toString()
                user.user_department = it.child(username).child("user_department").value.toString()
                user.user_mobile = it.child(username).child("user_mobile").value.toString()
                user.user_address = it.child(username).child("user_address").value.toString()
                Log.d("AuthRepo",user.user_email.toString())
                Log.d("AuthRepo",user.user_name.toString())
                requestCall.userDetail = user

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
        private val TAG: String = "UserPreferencesRepository"

        // Boilerplate-y code for singleton: the private reference to this self
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = UserRepository()
                INSTANCE = instance
                instance
            }
        }
    }

}