package com.example.Wickie.features.claims

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
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
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import java.io.File
import java.util.*
import com.example.Wickie.databinding.ActivityClaimsformBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat


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

            //viewModel.create(date,type,reason,amount)
            viewModel.update(date,type,reason,amount,"3")


        }


        //var textTypeDropdown = findViewById<TextInputLayout>(R.layout.typeItems.id.textTypeDropdown)

        //val editTextDate = findViewById<EditText>(R.id.editTextDate)
        //val imageButtonAttachment = findViewById<ImageView>(R.id.imageButtonAttachment)
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
            //set title for alert dialog
            builder.setTitle("Attachment Upload")
            //set message for alert dialog
            builder.setMessage("How would you upload your attachment?")

            //performing positive action
            builder.setPositiveButton("Gallery") { dialog, which ->
                dialog.dismiss()

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent,REQUEST_IMAGE_GALLERY)
            }
            //performing negative action
            builder.setNegativeButton("Camera"){dialog, which ->
                dialog.dismiss()

                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        val permission = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
                        }
                        else {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA)
                        }
                    }

                }

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

