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
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.core.view.isVisible
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.util.*
import com.example.Wickie.databinding.ActivityClaimsformBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern


class ClaimsFormActivity:BaseActivity() {
    private lateinit var binding : ActivityClaimsformBinding
    private val REQUEST_IMAGE_GALLERY = 132
    private val REQUEST_IMAGE_CAMERA = 142
    var itemList = arrayOf("transport", "meal", "phone")


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

        binding.buttonRequest.setOnClickListener {
            validate()
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

    //function to check date
    fun validateDate(date: String?): Boolean {
        val pattern: Pattern? = null
        var matcher: Matcher? = null
        matcher = pattern!!.matcher(date)
        return if (matcher.matches()) {
            matcher.reset()
            if (matcher.find()) {
                val day = matcher.group(1)
                val month = matcher.group(2)
                val year = matcher.group(3).toInt()
                if (day == "31" &&
                    (month == "4" || month == "6" || month == "9" || month == "11" || month == "04" || month == "06" || month == "09")
                ) {
                    false // only 1,3,5,7,8,10,12 has 31 days
                } else if (month == "2" || month == "02") {
                    //leap year
                    if (year % 4 == 0) {
                        if (day == "30" || day == "31") {
                            false
                        } else {
                            true
                        }
                    } else {
                        if (day == "29" || day == "30" || day == "31") {
                            false
                        } else {
                            true
                        }
                    }
                } else {
                    true
                }
            } else {
                false
            }
        } else {
            false
        }
    }

    //function to check the edit texts
    fun validate() : Boolean{

        if (binding.editTextDate.text != null) {

            if (validateDate(binding.editTextDate.text.toString())) {
                if (binding.editTextAmount.text != null) {
                    if (binding.editTextAmount.text != null) {
                        Toast.makeText(this, "All conditions satisfied", Toast.LENGTH_SHORT)
                        return true
                    }

                    else {
                        Toast.makeText(this, "Reason is empty", Toast.LENGTH_SHORT)
                        return false
                    }

                }

                else {
                    Toast.makeText(this, "Amount is empty", Toast.LENGTH_SHORT)
                    return false
                }
            }
            else {
                Toast.makeText(this, "Spend date is in invalid format", Toast.LENGTH_SHORT)
                return false
            }
        }
        else {
            Toast.makeText(this, "Spend date is empty", Toast.LENGTH_SHORT)
            return false
        }

    }

}

