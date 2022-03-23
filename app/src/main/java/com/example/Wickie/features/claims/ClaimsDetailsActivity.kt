package com.example.Wickie.features.claims

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.databinding.ClaimsDetailsBinding
import com.example.Wickie.features.home.MainActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.Serializable

class ClaimsDetailsActivity : BaseActivity() {
    private lateinit var binding : ClaimsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ClaimsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val claimObj  = intent.getSerializableExtra("claimObj") as? Claim
        if (claimObj != null) {
            Log.d("claimdetail",claimObj.imgUrl.toString())
        }
        val claimDetailViewModel: claimDetailsViewModel by viewModels {
            ClaimDetailsModelFactory(claimObj)
        }

        if (claimObj != null) {
            binding.textViewTitle.setText(claimDetailViewModel.claimObj.title)
            binding.textViewDate.text =claimDetailViewModel.claimObj.claimDate
            binding.textViewAmount.text = "$"+claimDetailViewModel.claimObj.amount
            binding.textViewType.text = claimDetailViewModel.claimObj.type
            binding.textViewStatus.text = claimDetailViewModel.claimObj.status

            if(claimDetailViewModel.claimObj.type == "transport"){
                binding.imageItem.setImageResource(R.drawable.transport_icon)
            } else if(claimDetailViewModel.claimObj.type == "phone"){
                binding.imageItem.setImageResource(R.drawable.phone_bill)
            } else {
                binding.imageItem.setImageResource(R.drawable.meal_icon)
            }
            Log.d("claimDetailsAct",claimDetailViewModel.claimObj.imgUrl.toString())
            val tst = downloadImg(claimDetailViewModel.claimObj.imgUrl.toString())
            setImageResource(tst)
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
                    openActivityWithIntent(MainActivity::class.java,"exists")
                }else if (it.status == 1)
                {
//                    show(it.message)
                    show("Something went wrong, Please try again later!")
                }
            }

        }
    }

    private fun downloadImg(imgUrl : String) : Bitmap
    {
        if (imgUrl == "")
        {
            var bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.wickie_success)
            return bitmap
        }
        var storageReference = FirebaseStorage.getInstance().reference.child("images").child("asif").child(imgUrl)
        val localfile = File.createTempFile("tempImage","png")
        show(storageReference.toString())

        Log.d("ClaimsFormAct",storageReference.toString())
        Toast.makeText(this, storageReference.toString(), Toast.LENGTH_LONG)

        var bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.wickie_success)

        storageReference.getFile(localfile).addOnSuccessListener {
            bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            Log.d("ClaimFormsActivity",it.toString())
            Log.d("ClaimFormsActivity","Success")
            setImageResource(bitmap)

        }.addOnFailureListener(){
            Log.d("ClaimFormsActivity",it.toString())
            setImageResource(bitmap)
        }
        Log.d("ClaimFormsActivity","Before Return")
        return bitmap
    }

    fun setImageResource(imageBitMap : Bitmap)
    {
        binding.ImageViewAttachment.setImageBitmap(imageBitMap)
    }


}