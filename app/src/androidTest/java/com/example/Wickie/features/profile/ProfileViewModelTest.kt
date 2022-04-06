package com.example.Wickie.features.profile

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.Wickie.data.source.SharedPrefRepo
import com.example.Wickie.data.source.UserRepository
import com.example.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileViewModelTest {

    private lateinit var viewModel : ProfileViewModel

@get:Rule
val instantTaskExecutorRole = InstantTaskExecutorRule()

@Before
fun setup()
{
    val context = ApplicationProvider.getApplicationContext<Context>()
    viewModel = ProfileViewModel(UserRepository(), SharedPrefRepo( context ))
}
@Test
fun retrieve()
{
    var msg = ""
    val result = viewModel.retrieve("asif").getOrAwaitValue().let {
        System.out.println(it.userDetail)
        msg = it.message
    }

    assertEquals("DATA FOUND", msg)
}}

