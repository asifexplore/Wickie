package com.example.Wickie.features.claims

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClaimViewModelTest{

    private lateinit var viewModel : ClaimViewModel

    @get:Rule
    val instantTaskExecutorRole = InstantTaskExecutorRule()

    @Before
    fun setup()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        viewModel = ClaimViewModel(SharedPrefRepo( context ), ClaimRepository())
    }
    /*
    * Testing to retrieve all claims requested. ViewModel function is used inside Claim Fragment.
    */
    @Test
    fun retrieveClaims()
    {
        var msg = ""
        var claimTotal = 0.0
        val result = viewModel.retrieve("asif").getOrAwaitValue().let {
            System.out.println(it.claimTotal.toString())
            msg = it.message
            claimTotal = it.claimTotal!!
        }
        System.out.println(claimTotal.toString())
        System.out.println("Hello StackOverflow")
        assertEquals("DATA FOUND", msg)
    }
}