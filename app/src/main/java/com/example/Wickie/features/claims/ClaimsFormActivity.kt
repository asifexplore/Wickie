package com.example.Wickie.features.claims

/*
*  Base Activity will be the parent activity, allowing other activities to inherit functionalites. Prevent the need to rewrite codes.
*
* the camera function is in the image View
*/

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.util.*
import com.example.Wickie.databinding.ActivityClaimsformBinding



class ClaimsFormActivity:BaseActivity() {
    private lateinit var binding : ActivityClaimsformBinding
    private val REQUEST_IMAGE_GALLERY = 132
    private val REQUEST_IMAGE_CAMERA = 142

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimsformBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        //create a dialog
        binding.imageButtonAttachment.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Attachment Upload")
            //set message for alert dialog
            builder.setMessage("How would you upload your attachment?")

            //call the gallery and ask permission for gallery
            builder.setPositiveButton("Gallery") { dialog, which ->
                dialog.dismiss()

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent,REQUEST_IMAGE_GALLERY)
            }
            //call the camera, check for permission to access the camera
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
    //set the image of the imageView after pick image or take picture
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            binding.imageButtonAttachment.setImageURI(data.data)
        }
        else if(requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            binding.imageButtonAttachment.setImageBitmap(data.extras?.get("data") as Bitmap)
        }
        else {
            Toast.makeText(this, "Cannot access gallery", Toast.LENGTH_SHORT).show()
        }
    }

}

