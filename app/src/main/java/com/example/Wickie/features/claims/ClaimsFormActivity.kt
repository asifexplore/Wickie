package com.example.Wickie.features.claims

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.Utils.ImageLibrary
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.databinding.ActivityClaimsformBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.storage.FirebaseStorage
import com.kofigyan.stateprogressbar.StateProgressBar
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var imageLibrary: ImageLibrary

    lateinit var imageURI : Uri
    // Name of File when Uploading
    private lateinit var fileName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimsformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileName = ""
        imageLibrary = ImageLibrary(this, this.packageManager, binding.imgViewUpload, fileName)
        val claimObj : Claim?
        if (getIntent().getExtras()?.getSerializable("claimObj") as? Claim != null)
        {
            claimObj  = getIntent().getExtras()?.getSerializable("claimObj") as? Claim
        }else
        {
            claimObj = Claim("","","","","","","","","")
        }

        imageLibrary.receiveImage(binding.imgViewUpload)

        // 0 = Add
        // 1 = Update
        val status : String
        if (intent.getStringExtra("status") != null)
        {
            status = "1"
        }
        else{
            status = "0"
        }

        val claimFormViewModel: ClaimsFormViewModel by viewModels {
            ClaimFormModelFactory(status, claimObj)
        }

        if (claimFormViewModel.currPageType == 1)
        {
            // Updating
            binding.textHello.text = "Update Claims"
            // Set edit txt field values
              // Downloads and Sets Image to ImageView | Possible to use coroutine in the future
            if (claimFormViewModel.currClaimObj != null) {
                Log.d("imageUrlTest",claimFormViewModel.currClaimObj.imgUrl.toString())
                imageLibrary.downloadImg(resources,claimFormViewModel.currClaimObj.imgUrl.toString())
                binding.editTextTitle.setText(claimFormViewModel.currClaimObj.title)
                binding.editTextAmount.setText(claimFormViewModel.currClaimObj.amount)
                binding.editTextCalendar.setText(claimFormViewModel.currClaimObj.claimDate)
                binding.editTextReason.setText(claimFormViewModel.currClaimObj.reason)
                binding.autoCompleteType.setText(claimFormViewModel.currClaimObj.type)
                Log.d("Tst",claimFormViewModel.currClaimObj.imgUrl.toString())
            }
        }
        else{
            // Inserting
            binding.textHello.text = "Inserting Claims"
        }

        binding.imgViewUpload.setOnClickListener {
            // Open Camera or Gallery
            galleryAlertBuilder()
        }

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

        // Binding name of states to array stored in ViewModel
        binding.progressBar.setStateDescriptionData(claimFormViewModel.descriptionData)
        // To Go Next on Horizontal Status Progress Bar
        binding.btnNext.setOnClickListener()
        {
            if (claimFormViewModel.pageStatus.value == 1) {

                // Add into Claims Object inside ViewModel
                claimFormViewModel.currClaimObj.title = binding.editTextTitle.text.toString()
                claimFormViewModel.currClaimObj.reason = binding.editTextReason.text.toString()
                claimFormViewModel.currClaimObj.amount = binding.editTextAmount.text.toString()
                claimFormViewModel.currClaimObj.type = binding.autoCompleteType.text.toString()
                claimFormViewModel.currClaimObj.claimDate = binding.textViewDate.text.toString()

                // Increment to Image Upload Section
                claimFormViewModel.incrementPageStatus()
            } else {
                // Upload Image
                imageLibrary.uploadImg(imageURI)
                // Update File Name
                claimFormViewModel.currClaimObj.imgUrl = fileName

                if (status.toInt() == 0) {
                    claimFormViewModel.create()
                        .observe(this, Observer {
                            if (it.status == 2) {
                                // Success
                                Log.d("ClaimsFormActivity", it.message.toString())
                                claimFormViewModel.incrementPageStatus()
                            } else {
                                if (it.message == "NO DATA FOUND") {
                                    Log.d("LoginActivity", it.status.toString())
                                    Log.d("LoginActivity", it.message.toString())

                                }
                            }
                        })
                } else {
                    claimFormViewModel.update()
                        .observe(this, Observer {
                            if (it.status == 2) {
                                // Success
                                Log.d("ClaimsFormActivity", "Update Success")
                                Log.d("ClaimsFormActivity", it.message.toString())
                                claimFormViewModel.incrementPageStatus()
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
                claimFormViewModel.decrementPageStatus()
            }
            // Update Items on Screen based on pageStatus on ViewModel
            claimFormViewModel.pageStatus.observe(this, Observer { newStatus ->
                pageVisibility(newStatus)
            })



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
        fileName = formatter.format(now)
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

    fun galleryAlertBuilder()
    {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Attachment Upload")
        //set message for alert dialog
        builder.setMessage("How would you upload your attachment?")

        //performing positive action
        builder.setPositiveButton("Gallery") { dialog, which ->
            dialog.dismiss()
            imageLibrary.useGallery()
        }
        //performing negative action
        builder.setNegativeButton("Camera"){dialog, which ->
            dialog.dismiss()

            imageLibrary.useCamera()
        }
        // Create the AlertDialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
           binding.imgViewUpload.setImageURI(data.data)
            imageURI = data.data!!
            Log.d("ClaimsFormActivity",imageURI.toString())
//            uploadImg()
        }
        else if(requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            binding.imgViewUpload.setImageBitmap(data.extras?.get("data") as Bitmap)
        }
        else {
            Toast.makeText(this, "Cannot access gallery", Toast.LENGTH_SHORT).show()
        }
    }
}

