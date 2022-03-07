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
import com.example.Wickie.features.home.ClaimFragment
import com.example.Wickie.features.home.MainActivity
import com.example.Wickie.hardware.CameraLibrary
import com.example.Wickie.hardware.GalleryLibrary
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.storage.FirebaseStorage
import com.kofigyan.stateprogressbar.StateProgressBar
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import androidx.lifecycle.Observer

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

        // Update Items on Screen based on pageStatus on ViewModel
        viewModel.pageType.observe(this, androidx.lifecycle.Observer { newStatus ->
            if (newStatus == 1) {
                binding.textHello.text = "Update Claims"
                // Set edit txt field values
                binding.editTextTitle.setText(viewModel.chosenClaim.title)
                binding.editTextAmount.setText(viewModel.chosenClaim.amount)
                binding.editTextCalendar.setText(viewModel.chosenClaim.claimDate)
                binding.editTextReason.setText(viewModel.chosenClaim.reason)
                binding.autoCompleteType.setText(viewModel.chosenClaim.type)
                // Downloads and Sets Image to ImageView | Possible to use coroutine in the future
                downloadImg("2022_02_24_08_26_21")
            }

            // Insert Data into Respective Edit Texts
        })

        // Binding name of states to array stored in ViewModel
        binding.progressBar.setStateDescriptionData(viewModel.descriptionData)
        // To Go Next on Horizontal Status Progress Bar
        binding.btnNext.setOnClickListener()
        {
            if (viewModel.pageStatus.value == 1) {
                // Increment to Image Upload Section
                viewModel.incrementPageStatus()
            } else {
                // Obtain Data From Form
                // Submitting to Firebase
                var title = binding.editTextTitle.text.toString()
                var reason = binding.editTextReason.text.toString()
                var amount = binding.editTextAmount.text.toString()
                var type = binding.autoCompleteType.text.toString()
                var imgUrl = "No Img Yet"
                var claimDate = binding.textViewDate.text.toString()

                if (viewModel.pageType.equals(0)) {
                    viewModel.create(title, reason, amount, type, imgUrl, claimDate)
                        .observe(this, Observer {
                            if (it.status == 2) {
                                // Success
                                Log.d("ClaimsFormActivity", it.message.toString())
                                viewModel.incrementPageStatus()
                            } else {
                                if (it.message == "NO DATA FOUND") {
                                    Log.d("LoginActivity", it.status.toString())
                                    Log.d("LoginActivity", it.message.toString())

                                }
                            }
                        })
                } else {
                    viewModel.update(title, reason, amount, type, imgUrl, claimDate)
                        .observe(this, Observer {
                            if (it.status == 2) {
                                // Success
                                Log.d("ClaimsFormActivity", "Update Success")
                                Log.d("ClaimsFormActivity", it.message.toString())
                                viewModel.incrementPageStatus()
                            } else {
                                if (it.message == "NO DATA FOUND") {
                                    Log.d("ClaimsFormActivity", it.status.toString())
                                    Log.d("ClaimsFormActivity", it.message.toString())

                                }
                            }
                        })
                }
            }
            // To Go Back on Horizontal Status Progress Bar
            binding.btnBack.setOnClickListener()
            {
                viewModel.decrementPageStatus()
            }
            // Update Items on Screen based on pageStatus on ViewModel
            viewModel.pageStatus.observe(this, androidx.lifecycle.Observer { newStatus ->
                pageVisibility(newStatus)
            })

            // Dropdown Items for Claims Form
            val types = resources.getStringArray(R.array.types)
            val arrayAdapter =
                ArrayAdapter(applicationContext, R.layout.claims_dropdown_items, types)
            binding.autoCompleteType.setAdapter(arrayAdapter)

            // Date Picker Initalization
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                // Respond to positive button click.
                binding.editTextCalendar.setText(datePicker.headerText.toString())
            }

            binding.editTextCalendar.setOnClickListener()
            {
                // Open Calendar DialogBox
                datePicker.show(supportFragmentManager, "DatePickerDialogBox");
            }


            // Btn Home to Redirect User to Claims Screen, when Claims are added successfully
            binding.btnHome.setOnClickListener()
            {
                // Intent to MainActivity and call the Claim Fragment
            }
        }
    }

    private fun page1(status:Boolean)
    {
        if (status)
        {
            binding.textView.visibility = View.VISIBLE
            binding.textInputLayoutTitle.visibility = View.VISIBLE
            binding.editTextTitle.visibility = View.VISIBLE
            binding.textViewCost.visibility = View.VISIBLE
            binding.textInputLayoutAmount.visibility = View.VISIBLE
            binding.editTextAmount.visibility = View.VISIBLE
            binding.textViewType.visibility = View.VISIBLE
            binding.textInputLayoutType.visibility = View.VISIBLE
            binding.autoCompleteType.visibility = View.VISIBLE
            binding.textViewDate.visibility = View.VISIBLE
            binding.textInputLayout.visibility = View.VISIBLE
            binding.editTextCalendar.visibility = View.VISIBLE
            binding.textViewReason.visibility = View.VISIBLE
            binding.textInputLayoutReason.visibility = View.VISIBLE
            binding.editTextReason.visibility = View.VISIBLE
            binding.btnBack.visibility = View.INVISIBLE
            binding.imgViewUpload.visibility = View.GONE
        }else
        {
            binding.textView.visibility = View.INVISIBLE
            binding.textInputLayoutTitle.visibility = View.INVISIBLE
            binding.editTextTitle.visibility = View.INVISIBLE
            binding.textViewCost.visibility = View.INVISIBLE
            binding.textInputLayoutAmount.visibility = View.INVISIBLE
            binding.editTextAmount.visibility = View.INVISIBLE
            binding.textViewType.visibility = View.INVISIBLE
            binding.textInputLayoutType.visibility = View.INVISIBLE
            binding.autoCompleteType.visibility = View.INVISIBLE
            binding.textViewDate.visibility = View.INVISIBLE
            binding.textInputLayout.visibility = View.INVISIBLE
            binding.editTextCalendar.visibility = View.INVISIBLE
            binding.textViewReason.visibility = View.INVISIBLE
            binding.textInputLayoutReason.visibility = View.INVISIBLE
            binding.editTextReason.visibility = View.INVISIBLE
        }
    }

    private fun pageVisibility(newStatus: Int)
    {
        // Set Visibility and Invisibility Accordingly
        if (newStatus == 1)
        {
            // Details Page
            binding.progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
            page1(true)
            binding.btnNext.text = "Next"

        }else if (newStatus == 2)
        {
            binding.progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
            page1(false)
            binding.imgViewUpload.visibility = View.VISIBLE
            binding.btnBack.visibility = View.VISIBLE
            binding.btnNext.text = "Submit"
        }else{
            binding.progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
            binding.imgViewUpload.setImageDrawable(getResources().getDrawable(R.drawable.wickie_success))
            binding.btnHome.visibility = View.VISIBLE
            binding.imgViewUpload.visibility = View.VISIBLE
            binding.btnBack.visibility = View.INVISIBLE
            binding.btnNext.visibility = View.INVISIBLE
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

    private fun downloadImg(imgUrl : String )
    {
        var storageReference = FirebaseStorage.getInstance().reference.child("images").child("asif").child(imgUrl)
        val localfile = File.createTempFile("tempImage","png")
        show(storageReference.toString())

        Log.d("ClaimsFormAct",storageReference.toString())
        Toast.makeText(this, storageReference.toString(),Toast.LENGTH_LONG)

        storageReference.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imgViewUpload.setImageBitmap(bitmap)

        }.addOnFailureListener(){
            show("Image not downloaded successfully")
            Log.d("ClaimFormsActivity",it.toString())
        }
    }
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

