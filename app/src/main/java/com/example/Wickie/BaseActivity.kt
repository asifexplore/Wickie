package com.example.Wickie


import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.Wickie.data.source.*

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
}