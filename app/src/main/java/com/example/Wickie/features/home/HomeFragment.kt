package com.example.Wickie.features.home
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R
import com.example.Wickie.databinding.FragmentHomeBinding

/*
*  Home Fragment will be the Activity for the Home Menu Screen
*  using other activities.
*
* Functions Within:
* ==========================================================================
* Function Name: openActivity(classProv: Class<*>?)
* Function Purpose: Intent to Other Activity
* Function Arguments: Class (Activity)
* Results:
*         Success: Goes to next page
*         Failed:
*---------------------------------------------------
*/



class HomeFragment:Fragment(R.layout.fragment_home) {
    private var binding: FragmentHomeBinding? = null
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var notif: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = FragmentHomeBinding.inflate(layoutInflater)
//        binding.recyclerViewEventList.layoutManager = LinearLayoutManager(this.context)
//
//            var arrBanner: ArrayList<Banner> = ArrayList()
//            arrBanner.add(Banner("event_banner1.jpg",R.drawable.event_banner1))
//            var adapter = EventAdapter(arrBanner)
//        binding.recyclerViewEventList.adapter = adapter


    }

}
