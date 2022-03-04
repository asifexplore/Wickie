package com.example.Wickie.features.home
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R
import com.example.Wickie.databinding.FragmentClaimsBinding
import com.example.Wickie.BaseActivity
import com.example.Wickie.features.claims.ClaimsFormActivity
import com.example.Wickie.features.profile.ProfileActivity

/*
*  Home Fragment will be the Activity for the Home Menu Screen
*  using other activities.
*
* Functions Within:
* ==========================================================================
* Function Name: Recycler View
* Function Purpose: Display Banner
* Function Arguments: Class (Activity)
* Results:
*         Success: Goes to next page
*         Failed:
*---------------------------------------------------
*/


class ClaimFragment:Fragment() {
    private lateinit var binding : FragmentClaimsBinding
    private lateinit var recyclerview: RecyclerView;
    private lateinit var adapter: ClaimAdapter; //Call my Adapter
    private lateinit var claims:ArrayList<Claim>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentClaimsBinding.inflate(inflater, container, false)
        //run recycler View
        recyclerview = binding.recyclerViewClaimsList
        recyclerview.layoutManager = LinearLayoutManager(this.context)
        claims = ArrayList<Claim>()
        claims.add(Claim("Transport Claim", "Site Visit: 23-05-2018","Pending",12.90, R.drawable.ic_transport_foreground))
        claims.add(Claim("Phone Bill", "Site Visit: 23-06-2018","Approved",12.90, R.drawable.ic_transport_foreground))
        claims.add(Claim("Transport", "Site Visit: 23-07-2018","Rejected",110.90, R.drawable.ic_transport_foreground))
        // This will pass the ArrayList to our Adapter
        adapter = ClaimAdapter(claims)
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        val root: View = binding.root

        binding.fab.setOnClickListener {
            val intent = Intent(context, ClaimsFormActivity::class.java)
            startActivity(intent)
        }
        return root
    }
}
