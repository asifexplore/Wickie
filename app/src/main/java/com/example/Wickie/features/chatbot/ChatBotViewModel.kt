package com.example.Wickie.features.chatbot

import androidx.lifecycle.ViewModel
import com.example.Wickie.data.source.ChatbotRepository
/*
*  ChatBotViewModel contains the function to connect to heroku webserver,
*
* Functions Within:
* ==========================================================================
* Function Name: connect
* Function Purpose: connect to heroku server
* Function Arguments: takes in the APIService as an argument
* Results:
*         Success: Connection established to the server
*         Failed:
*---------------------------------------------------
*/
class ChatBotViewModel( )  : ViewModel() {

    private val ChatbotRepository : ChatbotRepository = ChatbotRepository()


    fun connect(): APIService {
        return ChatbotRepository.connect()
    }

    fun receive() {
        ChatbotRepository.receive()
    }

}