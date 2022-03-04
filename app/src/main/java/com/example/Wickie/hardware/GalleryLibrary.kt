package com.example.Wickie.hardware

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
/*
*   GalleryLibrary is responsible for gallery realted activities
*
* Functions Within:
*  ==========================================================================
* Function Name: useGallery
* Function Purpose: ask for permission to use the gallery and start the gallery intent
* Function Arguments:
* Results:
*         Success: Proceed to galery intent
*         Failed: Toast to indicate cannot access gallery
*---------------------------------------------------

 */
class GalleryLibrary (activity: Activity, packageManager: PackageManager) {
    private val REQUEST_IMAGE_GALLERY = 132
    private lateinit var activity : Activity;
    private lateinit var packageManager : PackageManager;

    init{
        this.activity = activity
        this.packageManager = packageManager
    }

    fun useGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.activity.startActivityForResult(intent,REQUEST_IMAGE_GALLERY)
    }


}