package com.example.Wickie

import android.content.Intent
import android.media.audiofx.BassBoost
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/*
*  Base Activity will be the parent activity, allowing other activities to inherit functionalites. Prevent the need to rewrite codes.
*
* Functions Within:
* ==========================================================================
* Function Name: openActivity(classProv: Class<*>?)
* Function Purpose: Intent to Other Activity
* Function Arguments: Class (Activity)
* Results:
*         Success: Goes to next page
*         Failed:
*---------------------------------------------------
*/

open class BaseActivity : AppCompatActivity() {

    protected fun openActivity(classProv: Class<*>?)
    {
        val intent = Intent(this, classProv)
        startActivity(intent)
    }

    protected fun openActivityWithIntent(classProv: Class<*>?, argument1: String?){
        val intent = Intent(this,classProv)
        startActivity(intent.putExtra("username",argument1))
    }

    protected fun show(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

}