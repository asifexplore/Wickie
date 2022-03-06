package com.example.Wickie.features.claims

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.Validation
import com.example.Wickie.databinding.ActivityClaimsformBinding
import com.example.Wickie.features.home.Claim
import com.example.Wickie.features.home.ClaimFragment
import com.example.Wickie.features.home.MainActivity
import com.example.Wickie.hardware.CameraLibrary
import com.example.Wickie.hardware.GalleryLibrary
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/*
*   ClaimsFormActivity will be the activity to handle the logic for submitting a claim
*
* Functions Within:
*  ==========================================================================
* Function Name: uploadImg
* Function Purpose: Uploads the image
* Function Arguments: no arguments
* Results:
*         Success: Image uploaded
*         Failed:
*---------------------------------------------------
* ==========================================================================
* Function Name: downloadImg
* Function Purpose: Download the image
* Function Arguments: no arguments
* Results:
*         Success: Image downloaded
*         Failed:
*---------------------------------------------------
* ==========================================================================
* Function Name: onActivityResult
* Function Purpose: Update the ImageView image to the image picked from gallery or taken by camera
* Function Arguments: requestCode (int), resultCode (int) and data (Intent)
* Results:
*         Success: Imageview updated to selected image
*         Failed:
*---------------------------------------------------
*/


class ClaimsFormActivity:BaseActivity() {
    private lateinit var binding : ActivityClaimsformBinding
    private val REQUEST_IMAGE_GALLERY = 132
    private val REQUEST_IMAGE_CAMERA = 142

    lateinit var imageURI : Uri
    private lateinit var viewModel: ClaimViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimsformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ClaimViewModel::class.java)
        // Binding name of states to array stored in ViewModel
        binding.ProgressBar.setStateDescriptionData(viewModel.descriptionData)
        // To Go Next on Horizontal Status Progress Bar
        binding.btnNext.setOnClickListener()
        {
            viewModel.incrementPageStatus()
        }
        // To Go Back on Horizontal Status Progress Bar
        binding.btnBack.setOnClickListener()
        {
            viewModel.decrementPageStatus()
        }
        // Update Items on Screen based on pageStatus on ViewModel
         viewModel.pageStatus.observe(this, androidx.lifecycle.Observer {
           newStatus -> pageVisibility(newStatus)
         })


        }

    private fun pageVisibility(newStatus: Int)
    {
        // Set Visibility and Invisibility Accordingly
        if (newStatus == 0)
        {
            // Details Page
            binding.textView.visibility = View.VISIBLE
            binding.titleEditText.visibility = View.VISIBLE
            binding.btnBack.visibility = View.INVISIBLE
        }else
        {
            binding.textView.visibility = View.INVISIBLE
            binding.titleEditText.visibility = View.INVISIBLE
            binding.imgViewUpload.visibility = View.VISIBLE
            binding.btnBack.visibility = View.VISIBLE
        }
    }



    private fun uploadImg()
    {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Files...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        // Need Username
        var storageReference = FirebaseStorage.getInstance().getReference("images/asif/$fileName")
        storageReference.putFile(imageURI).addOnSuccessListener {
            show("Image Uploaded Successfully")
            if(progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener{
            show("Image Not Uploaded Successfully. Please try again later. ")
            if(progressDialog.isShowing) progressDialog.dismiss()
        }
    }

//    private fun downloadImg()
//    {
//        var storageReference = FirebaseStorage.getInstance().reference.child("images/2022_02_24_08_26_21")
//        val localfile = File.createTempFile("tempImage","png")
//        show(storageReference.toString())
//
//        Log.d("ClaimsFormAct",storageReference.toString())
//        Toast.makeText(this, storageReference.toString(),Toast.LENGTH_LONG)
//
//        storageReference.getFile(localfile).addOnSuccessListener {
//            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
//            binding.imageButtonAttachment.setImageBitmap(bitmap)
//
//        }.addOnFailureListener(){
//            show("Image not downloaded successfully")
//        }
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
//            binding.imageButtonAttachment.setImageURI(data.data)
//            imageURI = data.data!!
//            Log.d("ClaimsFormActivity",imageURI.toString())
//            uploadImg()
//        }
//        else if(requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
//            binding.imageButtonAttachment.setImageBitmap(data.extras?.get("data") as Bitmap)
//        }
//        else {
//            Toast.makeText(this, "Cannot access gallery", Toast.LENGTH_SHORT).show()
//        }
//    }




}

