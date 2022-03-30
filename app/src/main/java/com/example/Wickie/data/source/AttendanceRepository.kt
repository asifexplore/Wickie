package com.example.Wickie.data.source

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.Attendance
import com.example.Wickie.data.source.data.RequestAttendanceCall
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.Wickie.Utils.getCurrentTime
import java.text.SimpleDateFormat
import java.util.*


class AttendanceRepository {
    // Create Function to trigger Service
    val requestCall = RequestAttendanceCall()

    // Create Function to create attendance object into Firebase
    fun logIn(userID : String) : MutableLiveData<RequestAttendanceCall>
    {
        val status = "Pending"
        val mLiveData = MutableLiveData<RequestAttendanceCall>()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        // Date
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val currentDate = sdf.format(Date())

        val checkIn = getCurrentTime()
        val attendanceObj  = Attendance(currentDate,checkIn,"")

        database.get().addOnSuccessListener {
            Log.d("AttendanceRepo",it.toString())
            database.child("attendance").child(userID).child(currentDate).setValue(attendanceObj).addOnSuccessListener{
                requestCall.status = 2
                requestCall.message = "Logged In"
                mLiveData.postValue(requestCall)

            }.addOnFailureListener(){
                Log.d("AttendanceRepo", "Failed")
                requestCall.status = 1
                requestCall.message = "Logged Out"
                mLiveData.postValue(requestCall)
            }
        }
        return mLiveData
    }

    // Create Function to Store into Firebase
    fun logOut(userID : String) : MutableLiveData<RequestAttendanceCall>
    {
        val status = "Pending"
        val mLiveData = MutableLiveData<RequestAttendanceCall>()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        // Date
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val currentDate = sdf.format(Date())
        val checkOut = getCurrentTime()

        database.get().addOnSuccessListener {
            Log.d("AttendanceRepo",it.toString())
            database.child("attendance").child(userID).child(currentDate).child("endTime").setValue(checkOut).addOnSuccessListener{
                requestCall.status = 2
                requestCall.message = "Logged In"
                mLiveData.postValue(requestCall)

            }.addOnFailureListener(){
                Log.d("AttendanceRepo", "Failed")
                requestCall.status = 1
                requestCall.message = "Logged Out"
                mLiveData.postValue(requestCall)
            }
        }
        return mLiveData
    }
    companion object {
        // The usual for debugging
        private val TAG: String = "AttendanceRepository"

        // Boilerplate-y code for singleton: the private reference to this self
        @Volatile
        private var INSTANCE: AttendanceRepository? = null

        fun getInstance(context: Context): AttendanceRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = AttendanceRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}