package com.example.Wickie.features.home
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.databinding.FragmentClaimsBinding
import com.example.Wickie.features.claims.ClaimViewModel
import com.example.Wickie.features.claims.ClaimsFormActivity

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
    private lateinit var viewModel: ClaimViewModel
    var claims: ArrayList<Claim> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClaimsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ClaimViewModel::class.java)

        //run recycler View
        recyclerview = binding.recyclerViewClaimsList
        recyclerview.layoutManager = LinearLayoutManager(this.context)

        viewModel.retrieve().observe(viewLifecycleOwner) {
            // What do we do once we observe data from viewHolder?
            // Updated Recycler View
            claims.clear()
            if (it.message == "NO DATA FOUND") {
                binding.noDataTxtView.visibility = View.VISIBLE
                binding.recyclerViewClaimsList.visibility = View.INVISIBLE
            } else {
                binding.noDataTxtView.visibility = View.INVISIBLE
                binding.recyclerViewClaimsList.visibility = View.VISIBLE
                binding.balanceTxtView.text = "$" + it.claimTotal.toString()
                adapter = ClaimAdapter(it.claimArray as ArrayList<Claim>)
                recyclerview.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
        binding.claimsFAB.setOnClickListener()
        {
            val intent = Intent(context, ClaimsFormActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}
