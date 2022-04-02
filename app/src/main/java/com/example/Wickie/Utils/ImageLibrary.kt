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

class ImageLibrary (activity: BaseActivity, packageManager : PackageManager, imageURI: Uri?, imageView: ImageView?) {
    private val REQUEST_IMAGE_GALLERY = 132
    private val REQUEST_IMAGE_CAMERA = 142
    private var activity : Activity = activity
    private var packageManager : PackageManager = packageManager;
    private var imageURI : Uri? = imageURI;
    private var imageView : ImageView? = imageView

    public fun useCamera(photoFile: File) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val fileProvider = FileProvider.getUriForFile(activity, "com.example.Wickie.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA)
        } else {
            Toast.makeText(activity, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }

    }
    fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }


    fun useGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.activity.startActivityForResult(intent,REQUEST_IMAGE_GALLERY)
    }

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
            //show("Image Uploaded Successfully")
            Log.d("ImageLIbrary", "Success")
            Log.d("ImageLIbrary", imageURI.toString())
            if(progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener{
            //show("Image Not Uploaded Successfully. Please try again later. ")
            Log.d("ImageLIbrary", "Failure")
            Log.d("ImageLIbrary", imageURI.toString())
            if(progressDialog.isShowing) progressDialog.dismiss()
        }
        return fileName
    }

    fun downloadImg(resources: Resources, imgUrl : String, userID : String ) : Bitmap
    {
        if (imgUrl == "")
        {
            var bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.wickie_success)
            return bitmap
        }
        var storageReference = FirebaseStorage.getInstance().reference.child("images").child(userID).child(imgUrl)
        val localfile = File.createTempFile("tempImage","png")
        //activity.show(storageReference.toString())

        Log.d("ClaimsFormAct",storageReference.toString())
//        Toast.makeText(activity, storageReference.toString(), Toast.LENGTH_LONG).show()

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

    fun sendImage(intent: Intent, requestCode: Int, resultCode: Int, photoFile: File) {
        if(requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
            //val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            intent.putExtra("photoFile", photoFile)
            activity.startActivity(intent)

        }


    }
    fun checkReceive(receivingActivity: BaseActivity) : Boolean
    {
        val receive = receivingActivity.intent.extras?.get("photoFile")
        if (receive != null)
        {
            return true
        }
        return false
    }

    fun receiveImage(receivingActivity: BaseActivity, image: ImageView) : Uri {
        val receive = receivingActivity.intent.extras?.get("photoFile") as File
        val bitmap = BitmapFactory.decodeFile(receive.absolutePath)
        image.setImageBitmap(bitmap)

        return receive.toUri()

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



