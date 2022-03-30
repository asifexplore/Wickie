package com.example.Wickie.hardware
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
/*
*   CameraLibrary is responsible for camera related activities
*
* Functions Within:
*  ==========================================================================
* Function Name: useCamera
* Function Purpose: ask for permission to use the camera and start the camera intent
* Function Arguments:
* Results:
*         Success: Proceed to camera intent
*         Failed: Toast to indicate cannot access camera
*---------------------------------------------------

 */
class CameraLibrary (activity: Activity, packageManager: PackageManager){
    private val requestCamera = 142
    private lateinit var activity : Activity;
    private lateinit var packageManager : PackageManager;

    init{
        this.activity = activity
        this.packageManager = packageManager

    }


    public fun useCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhoto ->
            takePhoto.resolveActivity(this.packageManager)?.also {
                val consent = ContextCompat.checkSelfPermission(this.activity, Manifest.permission.CAMERA)
                if (consent != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.activity, arrayOf(Manifest.permission.CAMERA), 1)
                }
                else {
                    this.activity.startActivityForResult(takePhoto, requestCamera)

                }

            }

        }

    }

}