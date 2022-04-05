package com.example.Wickie.Utils

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*



class ImageLibrary (activity: BaseActivity, packageManager : PackageManager, imageURI: Uri?, imageView: ImageView?) {
    private val REQUEST_IMAGE_GALLERY = 132
    private val REQUEST_IMAGE_CAMERA = 142
    private var activity : Activity = activity
    private var packageManager : PackageManager = packageManager;
    private var imageURI : Uri? = imageURI;
    private var imageView : ImageView? = imageView

    /*
    * starts the camera feature of the device
    * check if have permission
    * if have permission, start the Camera Intent
    * else inform user that there is no permission
     */
    fun useCamera(photoFile: File) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val fileProvider = FileProvider.getUriForFile(activity, "com.example.Wickie.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA)
        } else {
            Toast.makeText(activity, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }

    }//useCamera()

    /*
    * create a file based on the filename
    * returns the file
     */
    fun getPhotoFile(fileName: String): File {
        val storageDirectory = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    } //getPhotoFile()


    /*
    start the gallery intent
     */
    fun useGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.activity.startActivityForResult(intent,REQUEST_IMAGE_GALLERY)
    }//useGallery()

    /*
    * responsible for uploading the image
    * returns the fileName
     */
    fun uploadImg(imageURI : Uri, userID : String) : String
    {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Uploading Files...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now).toString()
        // Need Username
        var storageReference = FirebaseStorage.getInstance().getReference("images/$userID/$fileName")
        storageReference.putFile(imageURI).addOnSuccessListener {
            if(progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener{
            if(progressDialog.isShowing) progressDialog.dismiss()
        }
        return fileName
    } //uploadImg()

    /*
    * responsible for downloading the image
    * returns the bitmap
     */
    fun downloadImg(resources: Resources, imgUrl : String, userID : String ) : Bitmap
    {
        if (imgUrl == "")
        {
            var bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.wickie_success)
            return bitmap
        }
        var storageReference = FirebaseStorage.getInstance().reference.child("images").child(userID).child(imgUrl)
        val localfile = File.createTempFile("tempImage","png")

        var bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.wickie_success)

        storageReference.getFile(localfile).addOnSuccessListener {
            bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            setImageResource(bitmap)

        }.addOnFailureListener(){
            setImageResource(bitmap)
        }
        return bitmap
    } //downloadImg()

    /*
    * set the image of the imageView
     */
    fun setImageResource(imageBitMap : Bitmap)
    {
        if (checkImage(imageView))
        {
            imageView?.setImageBitmap(imageBitMap)
        }
    } //setImageResource()

    /*
    * send the Image from one activity to another
    * checks for the outcome of the camera intent
    * if there is an image from the intent,
    * send the image to the next activity through the intent
     */
    fun sendImage(intent: Intent, requestCode: Int, resultCode: Int, photoFile: File) {
        if(requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
            intent.putExtra("photoFile", photoFile)
            activity.startActivity(intent)

        }
    } //sendImage()

    /*
    * checks if the activity receives an image
    * from an intent
     */
    fun checkReceive(receivingActivity: BaseActivity) : Boolean
    {
        val receive = receivingActivity.intent.extras?.get("photoFile")
        if (receive != null)
        {
            return true
        }
        return false
    } //checkReceive()

    /*
    * receive the image from the sending activity
    * set the imageView of the activity to the image
    * return the Uri of the image
     */
    fun receiveImage(receivingActivity: BaseActivity, image: ImageView) : Uri {
        val receive = receivingActivity.intent.extras?.get("photoFile") as File
        val bitmap = BitmapFactory.decodeFile(receive.absolutePath)
        image.setImageBitmap(bitmap)

        return receive.toUri()

    } //receiveImage()

    /*
    * check if the imageView is assigned
    * used by functions
     */
    fun checkImage(imageView: ImageView?): Boolean
    {
        if (imageView == null)
        {
            return false
        }
        return true
    } //checkImage()
}



