package com.example.Wickie.features.claims

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.data.Claim
import com.example.getOrAwaitValue
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class ClaimsFormActivityTest{

    // If wish to print out output, use the following code : System.out.println(msg)

    private lateinit var viewModel : ClaimsFormViewModel
    // Initialized Dummy Data to Test Create and Update
    private var claimObj = Claim("Testing Claims Form","Reason Testing Claims","500","Pending","meal","2022_04_02_17_55_49","Sat Apr 02 17:55:49 GMT+08:00 2022","01/04/2022","0")

    @get:Rule
    val instantTaskExecutorRole = InstantTaskExecutorRule()

    @Before
    fun setup()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        viewModel = ClaimsFormViewModel(0,claimObj,SharedPrefRepo( context ), ClaimRepository())
    }

    /*
    Testing Create Function inside ClaimsFormViewModel
    */
    @Test
    fun addClaim()
    {
        var msg = ""
        val result = viewModel.create("afiq", claimObj).getOrAwaitValue().let {
            msg = it.message
        }
        assertEquals("Add Success", msg)
    }
    /*
    Testing Update Function inside ClaimsFormViewModel
    Run this function after running "addClaim". As of now data is added to test account called "Afiq"
    */
    @Test
    fun updateClaim()
    {
        var msg = ""
        claimObj.title = "Updating Claim Title"
        val result = viewModel.update("afiq",claimObj).getOrAwaitValue().let {
            msg = it.message
        }
        assertEquals("Update Success", msg)
    }
}