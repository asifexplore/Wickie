package com.example.Wickie.features.login

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.Wickie.AuthRepository
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityUnitTest{

    // If wish to print out output, use the following code : System.out.println(msg)
    private lateinit var viewModel : LoginViewModel

    @get:Rule
    val instantTaskExecutorRole = InstantTaskExecutorRule()

    @Before
    fun setup()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        viewModel = LoginViewModel(AuthRepository(),SharedPrefRepo( context ))
    }

    /*
        Testing Login Function inside LoginViewModel
        */
    @Test
    fun testSuccessLogin()
    {
        var msg = ""
        val result = viewModel.login("afiq", "123").getOrAwaitValue().let {
            msg = it.message
        }
        assertEquals("DATA FOUND", msg)
    }

    @Test
    fun testFailLogin()
    {
        var msg = ""
        val result = viewModel.login("NoSuchUser", "123").getOrAwaitValue().let {
            msg = it.message
        }
        assertEquals("NO DATA FOUND", msg)
    }
}