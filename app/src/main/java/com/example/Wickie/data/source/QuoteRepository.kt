package com.example.Wickie.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.Wickie.data.source.data.Quote
import com.example.Wickie.data.source.data.RequestQuoteCall
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class QuoteRepository {
    fun retrieve() : MutableLiveData<RequestQuoteCall>
    {
        val mLiveData = MutableLiveData<RequestQuoteCall>()
        val requestCall = RequestQuoteCall()

        // In Progress
        requestCall.status = 1
        requestCall.message = "Fetching Data"
        mLiveData.value = requestCall

        var database : DatabaseReference = FirebaseDatabase.getInstance("https://wickie-3cfa2-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("quotes")

        database.get().addOnSuccessListener {
            var quote  = Quote()
            //Collects Quote Children Values: mon_quote in database
            quote.mon_quote = it.child("mon_quote").value.toString()
            requestCall.quoteDetail = quote
            mLiveData.postValue(requestCall)
        }.addOnFailureListener()
        {
            Log.d("AuthRepo", "Failed")
            requestCall.status = 1
            requestCall.message = "NO DATA FOUND"
            mLiveData.postValue(requestCall)
        }
        return mLiveData
    }
}