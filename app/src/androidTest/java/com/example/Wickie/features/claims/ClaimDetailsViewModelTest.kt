package com.example.Wickie.features.claims

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.data.Claim
import com.example.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class claimDetailsViewModelTest{

    // RUN THIS TEST CASE
    // If wish to print out output, use the following code : System.out.println(msg)

    private lateinit var viewModel : ClaimDetailsViewModel
    // Initialized Dummy Data to Test Create and Update
    private var claimObj = Claim("Testing Claims Form","Reason Testing Claims","500","Pending","meal","2022_04_02_17_55_49","Sat Apr 02 17:55:49 GMT+08:00 2022","01/04/2022","0")

    @get:Rule
    val instantTaskExecutorRole = InstantTaskExecutorRule()

    @Before
    fun setup()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        viewModel = ClaimDetailsViewModel(claimObj, SharedPrefRepo( context ), ClaimRepository())
    }
    /*
    Testing Delete Function inside claimDetailsViewModel. This function is implemented inside ClaimsDetailsActivity
    Changes "Status" to true when a specific claim is deleted.
    */
    @Test
    fun deleteClaim()
    {
        var msg = ""
        viewModel.deleteClaims(claimObj, "afiq").getOrAwaitValue().let{
            msg = it.message
        }
        assertEquals("Delete Success", msg)
    }

}