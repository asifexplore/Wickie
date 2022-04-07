package com.example

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> MutableLiveData<T>.getOrAwaitValue() : T
{

    var data : T? = null

    // Countdown is used to "wait" for data
    val latch = CountDownLatch(1)

    // Observer
    val observer = object : Observer<T>
    {
        override fun onChanged(t: T) {
            // Gets the live data value
            data = t
            // Removing current observer
            this@getOrAwaitValue.removeObserver(this)
            latch.countDown()
        }
    }

    this.observeForever(observer)

    try{
        // Waits till live data gets value
        if (!latch.await(1,TimeUnit.MINUTES))
        {
            throw TimeoutException("Mutable Live Data Never get its value")
        }
    }finally {
        this.removeObserver(observer)
    }

    return data as T
}

