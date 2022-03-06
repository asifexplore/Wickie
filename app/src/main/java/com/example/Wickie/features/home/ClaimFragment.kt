package com.example.Wickie.features.home
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.databinding.FragmentClaimsBinding
import com.example.Wickie.features.claims.ClaimViewModel
import com.example.Wickie.features.claims.ClaimsFormActivity
import com.example.Wickie.features.login.LoginViewModel

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
    private lateinit var claims:ArrayList<Claims>

    private lateinit var viewModel: ClaimViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentClaimsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ClaimViewModel::class.java)

        //run recycler View
        recyclerview = binding.recyclerViewClaimsList
        recyclerview.layoutManager = LinearLayoutManager(this.context)
//        claims = ArrayList<Claims>()
//        claims.add(Claims("Transport Claim", "Site Visit: 23-05-2018","Pending",12.90, R.drawable.ic_transport_foreground))
//        claims.add(Claims("Phone Bill", "Site Visit: 23-06-2018","Approved",12.90, R.drawable.ic_transport_foreground))
//        claims.add(Claims("Transport", "Site Visit: 23-07-2018","Rejected",110.90, R.drawable.ic_transport_foreground))

        // This will pass the ArrayList to our Adapter
//        adapter = ClaimAdapter(claims)
//        // Setting the Adapter with the recyclerview
//        recyclerview.adapter = adapter


        var claims: ArrayList<Claim> = ArrayList()

        viewModel.retrieve().observe(viewLifecycleOwner, {
            // What do we do once we observe data from viewHolder?
            // Updated Recycler View
            Log.d("ClaimFrag", it.toString())
            Log.d("ClaimFrag", it.message)
            Log.d("ClaimFrag", it.claimArray.toString())

            for (i in it.claimArray)
            {
                var newClaim: Claim = Claim()
                newClaim.type = i.type.toString()
                claims.add(newClaim)
                Log.d("ClaimFrag",  newClaim.type.toString())
            }

//             This will pass the ArrayList to our Adapter
        adapter = ClaimAdapter(claims)
//        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        })


        binding.fab.setOnClickListener()
        {
            val intent = Intent(context, ClaimsFormActivity::class.java)
            startActivity(intent)
        }

        val root: View = binding.root
        return root
    }

}
