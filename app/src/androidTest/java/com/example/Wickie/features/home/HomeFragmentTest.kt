package com.example.Wickie.features.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.Wickie.data.source.AttendanceRepository
import com.example.Wickie.data.source.ClaimRepository
import com.example.Wickie.data.source.QuoteRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.features.claims.ClaimFragModelFactory
import com.example.Wickie.features.claims.ClaimViewModel
import com.example.getOrAwaitValue
import junit.framework.TestCase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

//    private lateinit var HomeFragModel: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val instantTaskExecutorRole = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        viewModel =
            HomeViewModel(QuoteRepository(), AttendanceRepository(), SharedPrefRepo(context))
//        HomeFragModel =
//            HomeViewModelFactory(QuoteRepository(), AttendanceRepository(), SharedPrefRepo(context))
    }

    @Test
    fun showQuote() {
        var msg = ""
        var quoteDetail = ""
        val result = viewModel.showQuote().getOrAwaitValue().let {
            System.out.println(it.quoteDetail.toString())
            msg = it.message
            quoteDetail = it.quoteDetail.toString()!!
        }
        System.out.println(quoteDetail.toString())
        System.out.println("Hello StackOverflow")
        assertEquals("DATA FOUND", msg)
    }
}