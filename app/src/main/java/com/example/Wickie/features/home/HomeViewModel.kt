package com.example.Wickie.features.home



import androidx.lifecycle.*
import com.example.Wickie.data.source.AttendanceRepository
import com.example.Wickie.data.source.QuoteRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.data.LocationClass
import com.example.Wickie.data.source.data.RequestQuoteCall

class HomeViewModel(private val quoteRepository : QuoteRepository, private val attendanceRepository: AttendanceRepository  ,private val prefRepo: SharedPrefRepo)  : ViewModel() {


    var location : LocationClass = LocationClass(0.0,0.0)
    var currStatus : MutableLiveData<Boolean> = MutableLiveData()

    init {
        currStatus.value = getAttendanceStatus()
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

    fun getAttendanceStatus() : Boolean
    {
        currStatus.value = prefRepo.getAttendanceStatus() == 1
        return prefRepo.getAttendanceStatus() == 1
    }

    fun getUsername() : String
    {
        return prefRepo.getUsername()
    }

    // Function to check Location
    // 0 Error
    // 1 Incorrect Area
    // 2 Success
    fun setAttendance() : Int
    {
        //  Check if Logging-In or Out
        if (currStatus.value == true)
        {
            // Checking Out
            attendanceRepository.logOut(prefRepo.getUsername())
            currStatus.value = false
            // Add Into Shared Pref
            prefRepo.setAttendance(0)
            return 2
        }else
        {
            // Checking In | Check Lat & Lng
            // 1.4507 | 103.8232
            if (location.distance(location.latitude,location.longitude))
            {
                // Inside Correct Vicinity
                attendanceRepository.logIn(prefRepo.getUsername())
                currStatus.value = true
                // Add Into Shared Pref
                prefRepo.setAttendance(1)
                return 3
            }else
            {
                // Not at the right place
                return 1
            }
        }
    }
}
class HomeViewModelFactory(private val quoteRepository: QuoteRepository, private val attendanceRepository: AttendanceRepository ,private val prefRepo: SharedPrefRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(quoteRepository, attendanceRepository,prefRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}