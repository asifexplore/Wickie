package com.example.Wickie.features.chatbot

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.Wickie.data.source.ChatbotRepository
import com.example.Wickie.data.source.QuoteRepository
import com.example.Wickie.data.source.data.Quote
import com.example.Wickie.data.source.data.RequestQuoteCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatBotViewModel( )  : ViewModel() {

    private val ChatbotRepository : ChatbotRepository = ChatbotRepository()


    fun connect(): APIService {
        return ChatbotRepository.connect()
    }

    fun receive() {
        ChatbotRepository.receive()
    }

}