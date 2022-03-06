package com.example.Wickie.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.Wickie.data.source.QuoteRepository
import com.example.Wickie.data.source.data.Quote
import com.example.Wickie.data.source.data.RequestQuoteCall

class HomeViewModel( )  : ViewModel() {

    private val quoteRepository: QuoteRepository = QuoteRepository()

    fun showQuote() : MutableLiveData<RequestQuoteCall>
    {
        return quoteRepository.retrieve()
    }
}