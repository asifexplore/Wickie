package com.example.Wickie.Utils

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.features.claims.ClaimsFormActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/*
*   CameraLibrary is responsible for camera realted activities
*
* Functions Within:
*  ==========================================================================
* Function Name: useCamera
* Function Purpose: ask for permission to use the camera and start the camera intent
* Function Arguments:
 Results:
*         Success: Proceed to camera intent
*         Failed: Toast to indicate cannot access camera
*---------------------------------------------------

 */

class ImageLibrary (activity: BaseActivity, packageManager : PackageManager, imageView: ImageView?, fileName : String?) {
    private val REQUEST_IMAGE_GALLERY = 132
    private val REQUEST_IMAGE_CAMERA = 142
    private var activity : Activity = activity
    private var packageManager : PackageManager = packageManager;
    private var imageView : ImageView? = imageView
    private var fileName : String? = fileName

    public fun useCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhoto ->
            takePhoto.resolveActivity(this.packageManager)?.also {
                val consent = ContextCompat.checkSelfPermission(this.activity, Manifest.permission.CAMERA)
                if (consent != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.activity, arrayOf(Manifest.permission.CAMERA), 1)
                }
                else {
                    this.activity.startActivityForResult(takePhoto, REQUEST_IMAGE_CAMERA)
                }

            }

        }

    }

    fun useGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.activity.startActivityForResult(intent,REQUEST_IMAGE_GALLERY)
    }

    fun uploadImg(imageURI : Uri)
    {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Uploading Files...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        fileName = formatter.format(now)
        // Need Username
        var storageReference = FirebaseStorage.getInstance().getReference("images/asif/$fileName")
        storageReference.putFile(imageURI).addOnSuccessListener {
            //show("Image Uploaded Successfully")
            if(progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener{
            //show("Image Not Uploaded Successfully. Please try again later. ")
            if(progressDialog.isShowing) progressDialog.dismiss()
        }
    }

    fun downloadImg(resources: Resources, imgUrl : String) : Bitmap
    {
        if (imgUrl == "")
        {
            var bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.wickie_success)
            return bitmap
        }
        var storageReference = FirebaseStorage.getInstance().reference.child("images").child("asif").child(imgUrl)
        val localfile = File.createTempFile("tempImage","png")
        //activity.show(storageReference.toString())

        Log.d("ClaimsFormAct",storageReference.toString())
        Toast.makeText(activity, storageReference.toString(), Toast.LENGTH_LONG).show()

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
        if (checkImage(imageView))
        {
            imageView?.setImageBitmap(imageBitMap)
        }


    }

    fun sendImage(intent: Intent, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            if (data != null) {
                //val intent = Intent(activity, nextActivity::class.java)
                //intent.putExtra()
                val bitMapImage = data.extras?.get("data") as Bitmap
                intent.putExtra("BitmapImage", bitMapImage)
                activity.startActivity(intent)
            }
        }


    }

    fun receiveImage(imageView: ImageView?) {
        val bitmapIntent = activity.intent.getParcelableExtra<Bitmap>("BitmapImage")
        if (bitmapIntent != null && checkImage(imageView)) {
            imageView?.setImageBitmap(bitmapIntent)
        }
    }

    fun checkImage(imageView: ImageView?): Boolean
    {
        if (imageView == null)
        {
            return false
        }
        return true
    }
}



