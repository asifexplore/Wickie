package com.example.Wickie

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.media.audiofx.BassBoost
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.UserRepository
import com.example.Wickie.features.login.PreferenceUtil.Companion.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

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
    val sharedPrefRepo by lazy { SharedPrefRepo.getInstance(this) }
    val userRepository by lazy { UserRepository.getInstance(this) }

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

    protected fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}