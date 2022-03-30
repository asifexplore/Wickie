package com.example.Wickie.features.chatbot

import androidx.lifecycle.ViewModel
import com.example.Wickie.data.source.ChatbotRepository

class ChatBotViewModel( )  : ViewModel() {

    private val ChatbotRepository : ChatbotRepository = ChatbotRepository()


    fun connect(): APIService {
        return ChatbotRepository.connect()
    }

    fun receive() {
        ChatbotRepository.receive()
    }

}