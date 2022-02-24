package com.example.Wickie.features.home
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R
import com.example.Wickie.databinding.FragmentHomeBinding
import com.example.Wickie.features.profile.ProfileActivity

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


class HomeFragment:Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var recyclerview: RecyclerView;
    private lateinit var adapter: EventAdapter; //Call my Adapter
    private lateinit var notif: ArrayList<String> //List
    private lateinit var banner:ArrayList<Banner>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //run recycler View
        recyclerview = binding.recyclerViewEventList
        recyclerview.layoutManager = LinearLayoutManager(this.context)
        notif = ArrayList<String>()
        banner = ArrayList<Banner>()
        notif.add("ImageView1")
        notif.add("ImageView2")
        banner.add(Banner("String",R.drawable.event_banner1))
        banner.add(Banner("String2",R.drawable.event_banner1))
        banner.add(Banner("String3",R.drawable.event_banner1))
        banner.add(Banner("String4",R.drawable.event_banner1))
        banner.add(Banner("String5",R.drawable.event_banner1))
        banner.add(Banner("String6",R.drawable.event_banner1))
        banner.add(Banner("String7",R.drawable.event_banner1))
        banner.add(Banner("String8",R.drawable.event_banner1))
        // This will pass the ArrayList to our Adapter
        adapter = EventAdapter(banner)
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter


        //Profile Activity
        binding.profileButton.setOnClickListener {
            val intent = Intent(this.activity,ProfileActivity::class.java)
            startActivity(intent)
        }
        val root: View = binding.root
        return root
    }

}
