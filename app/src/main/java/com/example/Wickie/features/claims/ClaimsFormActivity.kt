package com.example.Wickie.features.claims

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.Utils.ImageLibrary
import com.example.Wickie.Validation
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.databinding.ActivityClaimsformBinding
import com.example.Wickie.features.home.MainActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.kofigyan.stateprogressbar.StateProgressBar
import java.io.File
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
    private val requestGallery = 132
    private val requestCamera = 142
    private lateinit var imageLibrary: ImageLibrary

    private var imageURI: Uri? = null
    // Name of File when Uploading
    private lateinit var fileName : String
    private lateinit var photoFile: File
    private val FILENAME = "photo.jpg"

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimsformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileName = ""

//        imageLibrary = ImageLibrary(this, this.packageManager, binding.imgViewUpload, fileName)
        imageLibrary = ImageLibrary(this, this.packageManager, imageURI, binding.imgViewUpload)

        val claimObj : Claim?
        if (getIntent().getExtras()?.getSerializable("claimObj") as? Claim != null)
        {
            claimObj  = getIntent().getExtras()?.getSerializable("claimObj") as? Claim
        }else
        {
            claimObj = Claim("","","","","","","","","")
        }
        //checks if receive image from intent
        if (imageLibrary.checkReceive(this))
        {
            imageURI = imageLibrary.receiveImage(this,binding.imgViewUpload)
        }


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
            ClaimFormModelFactory(status, claimObj, sharedPrefRepo,claimRepository)
        }

        if (claimFormViewModel.currPageType == 1)
        {
            // Updating
            binding.textHello.text = "Update Claims"
            // Set edit txt field values
              // Downloads and Sets Image to ImageView | Possible to use coroutine in the future
            if (claimFormViewModel.currClaimObj != null) {
                Log.d("imageUrlTest",claimFormViewModel.currClaimObj.imageUrl.toString())
                imageLibrary.downloadImg(resources,claimFormViewModel.currClaimObj.imageUrl.toString(), sharedPrefRepo.getUsername())
                binding.editTextTitle.setText(claimFormViewModel.currClaimObj.title)
                binding.editTextAmount.setText(claimFormViewModel.currClaimObj.amount)
                binding.editTextCalendar.setText(claimFormViewModel.currClaimObj.claimDate)
                binding.editTextReason.setText(claimFormViewModel.currClaimObj.reason)
                binding.autoCompleteType.setText(claimFormViewModel.currClaimObj.type)
                Log.d("Tst",claimFormViewModel.currClaimObj.imageUrl.toString())
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

        datePicker.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener<Long?> { selection -> // Do something...
            val dateString: String = DateFormat.format("dd/MM/yyyy", Date(selection)).toString()
            binding.editTextCalendar.setText(dateString)

        })

        binding.editTextCalendar.setOnClickListener()
        {
            // Open Calendar DialogBox
            datePicker.show(supportFragmentManager, "DatePickerDialogBox");
        }

        // Binding name of states to array stored in ViewModel
        binding.progressBar.setStateDescriptionData(claimFormViewModel.descriptionData)
        // To Go Next on Horizontal Status Progress Bar
        val validation = Validation()

        binding.btnNext.setOnClickListener()
        {
            if (claimFormViewModel.pageStatus.value == 1) {

                // Add into Claims Object inside ViewModel
                claimFormViewModel.currClaimObj.title = binding.editTextTitle.text.toString()
                claimFormViewModel.currClaimObj.reason = binding.editTextReason.text.toString()
                claimFormViewModel.currClaimObj.amount = binding.editTextAmount.text.toString()
                claimFormViewModel.currClaimObj.type = binding.autoCompleteType.text.toString()
                claimFormViewModel.currClaimObj.claimDate = binding.editTextCalendar.text.toString()



                // Increment to Image Upload Section
                val claimValid = validation.validateClaim(binding.editTextTitle, binding.editTextAmount, binding.autoCompleteType,binding.editTextCalendar, binding.editTextReason)
                if (claimValid) {
                    claimFormViewModel.incrementPageStatus()
                }

            } else {
                // Upload Image
                var filename = ""
                imageURI?.let { it1 -> filename = imageLibrary.uploadImg(it1, sharedPrefRepo.getUsername()) }
                // Update File Name
                if (filename != "")
                {
                    claimFormViewModel.currClaimObj.imageUrl = filename
                }

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
                openActivityWithIntent(MainActivity::class.java,"claimCompleted", "true")
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
            binding.btnBack.visibility = View.GONE
            binding.imgViewUpload.visibility = View.GONE
        }else
        {
            binding.textView.visibility = View.GONE
            binding.textInputLayoutTitle.visibility = View.GONE
            binding.editTextTitle.visibility = View.GONE
            binding.textViewCost.visibility = View.GONE
            binding.textInputLayoutAmount.visibility = View.GONE
            binding.editTextAmount.visibility = View.GONE
            binding.textViewType.visibility = View.GONE
            binding.textInputLayoutType.visibility = View.GONE
            binding.autoCompleteType.visibility = View.GONE
            binding.textViewDate.visibility = View.GONE
            binding.textInputLayout.visibility = View.GONE
            binding.editTextCalendar.visibility = View.GONE
            binding.textViewReason.visibility = View.GONE
            binding.textInputLayoutReason.visibility = View.GONE
            binding.editTextReason.visibility = View.GONE
        }
    }

    /*
    * set the visibility of the pages
    * according to the state
     */
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
    } //pageVisibility()
    /*
    * Creates the popup dialog to allow user to choose
    * to upload through gallery or through the camera
     */

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

            photoFile = imageLibrary.getPhotoFile(FILENAME)
            imageLibrary.useCamera(photoFile)

        }
        // Create the AlertDialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }//galleryAlertBuilder()

    /*
    * set the imgViewUpload button or the thumbnail
    * in the ClaimsForm to the chosen picture
    * checks what action was taken
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == requestGallery && resultCode == Activity.RESULT_OK && data != null) {
            binding.imgViewUpload.setImageURI(data.data)
            imageURI = data.data!!
        }
        else if(requestCode == requestCamera && resultCode == Activity.RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.imgViewUpload.setImageBitmap(bitmap)
            imageURI = photoFile.toUri()
        }
        else {
            Toast.makeText(this, "Cannot access gallery", Toast.LENGTH_SHORT).show()
        }
    }//onActivityResult()

}

