package com.example.Wickie.features.claims

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.Utils.ImageLibrary
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.databinding.ClaimsDetailsBinding
import com.example.Wickie.features.home.MainActivity

/*
* ClaimsDetailsActivity is the activity that will
* allow the user to view, update and delete the claims they applied
 */
class ClaimsDetailsActivity : BaseActivity() {
    private lateinit var binding : ClaimsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ClaimsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val claimObj  = intent.getSerializableExtra("claimObj") as? Claim
        if (claimObj != null) {

        }
        val claimDetailViewModel: claimDetailsViewModel by viewModels {
            ClaimDetailsModelFactory(claimObj, sharedPrefRepo, claimRepository)
        }

        if (claimObj != null) {
            binding.textViewTitle.setText(claimDetailViewModel.claimObj.title)
            binding.textViewDate.text =claimDetailViewModel.claimObj.claimDate
            binding.textViewAmount.text = "$"+claimDetailViewModel.claimObj.amount
            binding.textViewType.text = claimDetailViewModel.claimObj.type
            binding.textViewStatusText.text = claimDetailViewModel.claimObj.status
            binding.ReasonDescription.text = claimDetailViewModel.claimObj.reason

            if(claimDetailViewModel.claimObj.type == "transport"){
                binding.imageItem.setImageResource(R.drawable.transport_icon)
            } else if(claimDetailViewModel.claimObj.type == "phone"){
                binding.imageItem.setImageResource(R.drawable.phone_bill)
            } else {
                binding.imageItem.setImageResource(R.drawable.meal_icon)
            }

            if (claimDetailViewModel.claimObj.status == "Approved")
            {
                binding.textViewStatusText.setTextColor(Color.parseColor("#00FF00"))
            }else if(claimDetailViewModel.claimObj.status == "Rejected")
            {
                binding.textViewStatusText.setTextColor(Color.parseColor("#FF0000"))
            }

            //initialise the ImageLibrary
            val imageLibrary = ImageLibrary(this, this.packageManager, null, binding.ImageViewAttachment)
            val tst = imageLibrary.downloadImg(resources, claimDetailViewModel.claimObj.imageUrl.toString(), sharedPrefRepo.getUsername())
            imageLibrary.setImageResource(tst)

            if (claimDetailViewModel.claimObj.status == "Approved" || claimDetailViewModel.claimObj.status == "Rejected")
            {
                binding.btnDelete.visibility = View.GONE
                binding.btnUpdate.visibility = View.GONE
            }
        }
        else
        {
            //Open Back Claim Fragment with Error Toast Message
        }

        binding.btnUpdate.setOnClickListener()
        {
            val intent2 = Intent(this, ClaimsFormActivity::class.java)
                .putExtra("status","1")
                .putExtra("claimID",intent.getStringExtra("claimID").toString())
                // Send Claim Object
                .putExtra("claimObj",claimObj)
            startActivity(intent2)
        }

        binding.btnDelete.setOnClickListener()
        {
            claimDetailViewModel.deleteClaims().observe(this){
                if (it.status == 2)
                {
                    // Success --> Intent
                    openActivityWithIntent(MainActivity::class.java,"claimCompleted", "true")
                }else if (it.status == 1)
                {
//                    show(it.message)
                    show("Something went wrong, Please try again later!")
                }
            }

        }
    }   //onCreate

    /*
    * sets the ImageViewAttachment to the bitmap image passed
     */
    fun setImageResource(imageBitMap : Bitmap)
    {
        binding.ImageViewAttachment.setImageBitmap(imageBitMap)
    } //setImageResource

}