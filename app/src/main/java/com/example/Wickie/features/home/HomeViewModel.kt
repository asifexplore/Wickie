package com.example.Wickie.features.home

import LocationUtils
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.Wickie.data.source.AttendanceRepository
import com.example.Wickie.data.source.QuoteRepository
import com.example.Wickie.data.source.data.Attendance
import com.example.Wickie.data.source.data.Location
import com.example.Wickie.data.source.data.Quote
import com.example.Wickie.data.source.data.RequestQuoteCall
import com.example.Wickie.services.NetworkService

class HomeViewModel()  : ViewModel() {

    private val quoteRepository: QuoteRepository = QuoteRepository()
    private val attendanceRepository: AttendanceRepository = AttendanceRepository()
    var location : Location = Location(0.0,0.0)
    var currStatus : MutableLiveData<Boolean> = MutableLiveData()

    init {
        currStatus.value = false
    }

    fun showQuote() : MutableLiveData<RequestQuoteCall>
    {
        return quoteRepository.retrieve()
    }

    fun addLocation(long : Double, lat: Double )
    {
        location.latitude = lat
        location.longitude = long
    }

    // Function to check Location
    // 0 Error
    // 1 Incorrect Area
    // 2 Success
    fun checkLocation() : Int
    {
        //  Check if Logging-In or Out
        if (currStatus.value == true)
        {
            // Checking Out
            return 2
        }else
        {
            // Checking In | Check Lat & Lng
            if (location.latitude == 1.4507 && location.longitude == 103.8232)
            {
//                var attendanceObj : Attendance = Attendance("","","")
                // Inside Correct Vicinity
                attendanceRepository.logIn()
                currStatus.value = true
            }else
            {
                Log.d("HomeViewModel Lat",location.latitude.toString())
                Log.d("HomeViewModel Lng",location.longitude.toString())
                // Not at the right place
                return 1
            }
        }

        return 0
    }

}