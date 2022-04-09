import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationUtils private constructor(){

    companion object {
        private var fusedLocationProviderClient: FusedLocationProviderClient?= null
        lateinit var location : MutableLiveData<Location>

        // using singleton pattern to get the locationProviderClient
        fun getInstance(appContext: Context): FusedLocationProviderClient {
            if (fusedLocationProviderClient == null)
                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(appContext)
            location = MutableLiveData()
            return fusedLocationProviderClient!!
        }

        @SuppressLint("MissingPermission")
        fun getCurrLocation() {
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    if (loc != null) {
                        location.value = loc
                    }
                }
        }
    }

}