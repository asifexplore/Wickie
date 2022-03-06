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
        binding.buttonRequest.setOnClickListener()
        {
            val date = binding.editTextDate.text.toString()
            val type = binding.typeItems.text.toString()
            val reason = binding.editTextReason.text.toString()
            val amount = binding.editTextAmount.text.toString()
            val inputs = arrayOf(binding.editTextDate, binding.editTextAmount, binding.typeItems, binding.editTextReason)
//            val validate = Validation(inputs)

//            if (validate.validateClaim(inputs)) {
//                val validationMessage = Toast.makeText(this, "All requirements are met", Toast.LENGTH_SHORT)
//                validationMessage.show()
//            }

            //viewModel.create(date,type,reason,amount)
            //viewModel.update(date,type,reason,amount,"3")


        }

        val calendar = Calendar.getInstance()
        val myYear = calendar.get(Calendar.YEAR)
        val myMonth = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var itemList = arrayOf("transport", "meal", "phone")

        var arrayAdapter = ArrayAdapter(this, R.layout.type_list, itemList)
        binding.typeItems.setAdapter(arrayAdapter)

        binding.editTextDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val display:String = dayOfMonth.toString() + "/" + (month+1) + "/" + year
                binding.editTextDate.setText(display)
            }, myYear, myMonth, day)
            datePickerDialog.show()
        }

        binding.imageButtonAttachment.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Attachment Upload")
            builder.setMessage("How would you upload your attachment?")

            //using the gallery feature
            builder.setPositiveButton("Gallery") { dialog, which ->
                dialog.dismiss()

                val gallery = GalleryLibrary(this, packageManager)
                gallery.useGallery()



            }
            //using the camera feature
            builder.setNegativeButton("Camera"){dialog, which ->
                dialog.dismiss()

                val camera = CameraLibrary(this, packageManager)
                camera.useCamera()

            }
            // Create the AlertDialog
            val dialog: AlertDialog = builder.create()
            dialog.show()
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

    private fun downloadImg()
    {
        var storageReference = FirebaseStorage.getInstance().reference.child("images/2022_02_24_08_26_21")
        val localfile = File.createTempFile("tempImage","png")
        show(storageReference.toString())

        Log.d("ClaimsFormAct",storageReference.toString())
        Toast.makeText(this, storageReference.toString(),Toast.LENGTH_LONG)

        storageReference.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageButtonAttachment.setImageBitmap(bitmap)

        }.addOnFailureListener(){
            show("Image not downloaded successfully")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            binding.imageButtonAttachment.setImageURI(data.data)
            imageURI = data.data!!
            Log.d("ClaimsFormActivity",imageURI.toString())
            uploadImg()
        }
        else if(requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            binding.imageButtonAttachment.setImageBitmap(data.extras?.get("data") as Bitmap)
        }
        else {
            Toast.makeText(this, "Cannot access gallery", Toast.LENGTH_SHORT).show()
        }
    }




}

