package com.example.Wickie

import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.Wickie.data.source.*

/*
*  Base Activity will be the parent activity, allowing other activities to inherit functionalites. Prevent the need to rewrite codes.
*  Also initialising repo instances for Singleton Pattern.
*
* Functions Within:
* ==========================================================================
* Function Name: openActivity(classProv: Class<*>?)
* Function Purpose: Intent to Other Activity
* Function Arguments: Class (Activity)
* Results:
*         Success: Goes to next page
*---------------------------------------------------
* Function Name: openActivityWithIntent(classProv: Class<*>?, argument1: String?, argument2 : String?)
* Function Purpose: Intent to Other Activity while passing variables
* Function Arguments:
*         Class (Activity)
*         argument1 would the key on putExtra in the Intent
*         argument2 would be the value of the putExtra in the Intent.
* Results:
*         Success: Goes to next page
*---------------------------------------------------
* Function Name: startLoadingDialogBox(text : String )
* Function Purpose: Function that displays ProgressDialogBox
* Function Arguments: variable text is the string that would be displayed on the ProgressDialogBox
* Results:
*         Success: Display ProgressDialogBox
*---------------------------------------------------
* Function Name: closeLoadingDialogBox()
* Function Purpose: Function that closes ProgressDialogBox if it exists
* Function Arguments: Nil
* Results:
*         Success: Closes ProgressDialogBox
*/

open class BaseActivity : AppCompatActivity() {
    val sharedPrefRepo by lazy { SharedPrefRepo.getInstance(this) }
    val userRepository by lazy { UserRepository.getInstance(this) }
    val quoteRepository by lazy { QuoteRepository.getInstance(this)  }
    val attendanceRepository by lazy { AttendanceRepository.getInstance(this)  }
    val authRepository by lazy { AuthRepository.getInstance(this)  }
    val claimRepository by lazy { ClaimRepository.getInstance(this) }

    protected fun openActivity(classProv: Class<*>?)
    {
        val intent = Intent(this, classProv)
        startActivity(intent)
    }

    protected fun openActivityWithIntent(classProv: Class<*>?, argument1: String?, argument2 : String?){
        val intent = Intent(this,classProv)
        startActivity(intent.putExtra(argument1, argument2))
    }


    protected fun show(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    protected fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    protected fun startLoadingDialogBox(text : String )
    {
        // Loading Dialog Box
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage(text)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }
    protected  fun closeLoadingDialogBox()
    {
        // Loading Dialog Box
        val progressDialog = ProgressDialog(this)
        if(progressDialog.isShowing) progressDialog.dismiss()
    }

}