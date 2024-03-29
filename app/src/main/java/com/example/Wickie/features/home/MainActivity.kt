package com.example.Wickie.features.home
import android.content.Intent
import android.os.*
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.Utils.ImageLibrary
import com.example.Wickie.databinding.ActivityMainBinding
import com.example.Wickie.features.claims.ClaimsFormActivity
import java.io.File

/*
* MainActivity is the activity that will host
* the three main fragments:
* HomeFragment(), CLaimsFragment() and the WickieFragment()
 */
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageLibrary: ImageLibrary
    lateinit var photoFile: File
    val FILE_NAME = "photo.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize view binding on activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomNavigationView.itemIconTintList = null
        setContentView(binding.root)

        //initialise ImageLibrary
        imageLibrary =  ImageLibrary(this, this.packageManager, null, null)

        /*
        * check if HomeFragment selects Submit Claims button
        * if yes, create a file and start the camera
         */
        if (intent.getBooleanExtra("cameraflag", false))
        {
            photoFile = imageLibrary.getPhotoFile(FILE_NAME)
            imageLibrary.useCamera(photoFile)
        }

        //Initialize Fragments for the Navigation Bar (Claims, Home , Settings)
        val homeFragment=HomeFragment()
        val claimFragment=ClaimFragment()
        val wickieFragment=WickieFragment()
        setCurrentFragment(homeFragment)
        //Set Home Button as Default Selected Item in the Home Menu Screen
        binding.bottomNavigationView.selectedItemId = R.id.home
        val claimCompleted = intent.getStringExtra("claimCompleted")
        if ( claimCompleted == "true")
        {
            binding.bottomNavigationView.selectedItemId = R.id.claim
            setCurrentFragment(claimFragment)
        }

        //If Claims has been created, redirect back to claims fragment (logic to check)
        val check = intent.getBooleanExtra("exist",false)
        if(check){
            binding.bottomNavigationView.selectedItemId = R.id.claim
            setCurrentFragment(claimFragment)
        }

        //If user sends message through Home Screen (Collects Message)
        val message = intent.getStringExtra("KEY")
        val check2 = intent.getBooleanExtra("WICKIE",false)
        if(check2){
            val bundle = Bundle()
            bundle.putString("KEY", message)
            bundle.putBoolean("notEmpty",true)
            wickieFragment.arguments = bundle
            binding.bottomNavigationView.selectedItemId = R.id.wickie
            setCurrentFragment(wickieFragment)

        }
        val happy = intent.getBooleanExtra("HAPPY",false)
        val tired = intent.getBooleanExtra("TIRED",false)
        if(happy){
            val bundle = Bundle()
            bundle.putString("HAPPY","Good Mood Today!")
            wickieFragment.arguments = bundle
            binding.bottomNavigationView.selectedItemId = R.id.wickie
            setCurrentFragment(wickieFragment)
        }
        if(tired){
            val bundle = Bundle()
            bundle.putString("TIRED","I am Tired")
            wickieFragment.arguments = bundle
            binding.bottomNavigationView.selectedItemId = R.id.wickie
            setCurrentFragment(wickieFragment)
        }


        //Else Display Home Menu Screen with navigation bar
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFragment)
                R.id.claim->setCurrentFragment(claimFragment)
                R.id.wickie->setCurrentFragment(wickieFragment)
            }
            true
        }

    }

    /*
    * send the Image from the Camera intent
    * to the ClaimsFormActivity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val intent = Intent(this, ClaimsFormActivity::class.java)
        imageLibrary.sendImage(intent, requestCode, resultCode, photoFile)
    }//onActivityResult

    /*
    * block the user from clicking on back
     */
    override fun onBackPressed() {
        show("Backpress is blocked on this screen")
    }//onBackPressed

}
