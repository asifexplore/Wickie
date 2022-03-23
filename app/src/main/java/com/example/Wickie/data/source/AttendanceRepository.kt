package com.example.Wickie.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.Attendance
import com.example.Wickie.data.source.data.RequestAttendanceCall
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.Wickie.Utils.getCurrentDate
import com.example.Wickie.Utils.getCurrentDateTime
import com.example.Wickie.Utils.getCurrentTime


class AttendanceRepository {
    // Create Function to trigger Service
    val requestCall = RequestAttendanceCall()

    // Create Function to Store into Firebase
    fun logIn() : MutableLiveData<RequestAttendanceCall>
    {
        val status = "Pending"
        val mLiveData = MutableLiveData<RequestAttendanceCall>()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        // Date
        val attendanceID = getCurrentDate()
        val checkIn = getCurrentTime()
        val attendanceObj  = Attendance(attendanceID,checkIn,"")

        database.get().addOnSuccessListener {
            Log.d("AttendanceRepo",it.toString())
            database.child("attendance").child("asif").child(attendanceID).setValue(attendanceObj).addOnSuccessListener{
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
}