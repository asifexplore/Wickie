package com.example.Wickie.features.login

import android.content.Context
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.Wickie.R
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest
{
    // LoginActivity is the Activity we want to test
    private lateinit var scenario : ActivityScenario<LoginActivity>

    // Function called before every test
    @Before
    fun setup()
    {
        scenario = launchActivity()
        // Activity is started
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun loginSuccess()
    {
        val username : String = "afiq"
        val passwod  : String = "123"
        // Setting Values
        onView(withId(R.id.editTextEmail)).perform(ViewActions.typeText(username))
        onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText(passwod))
        // Closing Keyboard
        Espresso.closeSoftKeyboard()
        // Clicking Sign In Button
        onView(withId(R.id.buttonSignIn)).perform(click())
    }
}
