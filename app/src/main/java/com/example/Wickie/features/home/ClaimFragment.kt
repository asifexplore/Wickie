package com.example.Wickie.features.home
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.databinding.FragmentClaimsBinding
import com.example.Wickie.features.claims.ClaimViewModel
import com.example.Wickie.features.claims.ClaimsFormActivity
import com.example.Wickie.features.claims.ViewClaimsActivity

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


class ClaimFragment:Fragment() , OnClaimsClickListener {
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

//        var claims: ArrayList<Claim> = ArrayList()

        viewModel.retrieve().observe(viewLifecycleOwner) {
            // What do we do once we observe data from viewHolder?
            // Updated Recycler View
            if (it.message == "NO DATA FOUND") {
                binding.noDataTxtView.visibility = View.VISIBLE
                binding.recyclerViewClaimsList.visibility = View.INVISIBLE
            } else {
                binding.noDataTxtView.visibility = View.INVISIBLE
                binding.recyclerViewClaimsList.visibility = View.VISIBLE
                for (i in it.claimArray) {
                    var newClaim: Claim = Claim(
                        i.title.toString(),
                        i.reason.toString(),
                        i.amount.toString(),
                        i.status.toString(),
                        i.type.toString(),
                        i.imgUrl.toString(),
                        i.createdDate.toString(),
                        i.claimDate.toString()
                    )
                    claims.add(newClaim)
                }
                binding.balanceTxtView.text = "$" + it.claimTotal.toString()
                //This will pass the ArrayList to our Adapter
                adapter = ClaimAdapter(claims,this)
                // Setting the Adapter with the recyclerview
                recyclerview.adapter = adapter
            }
        }
        binding.claimsFAB.setOnClickListener()
        {
            val intent = Intent(context, ClaimsFormActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onClaimsClickListener(position: Int) {
        val intent = Intent(context, ViewClaimsActivity::class.java)
        startActivity(intent
            .putExtra("title",claims[position].title)
            .putExtra("claimDate",claims[position].claimDate.toString())
            .putExtra("amount",claims[position].amount.toString())
            .putExtra("type",claims[position].type.toString())
            .putExtra("status",claims[position].status.toString())
        )
    }
}
