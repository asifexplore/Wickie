package com.example.Wickie.features.chatbot

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
/*
*  APIService is the interface to connect to heroku webserver,
* it will send the message of the user and retrieves the reply by the chatbot
*
* Functions Within:
* ==========================================================================
* Function Name: chatWithTheBot
* Function Purpose: sends the message through url "/chatInput="message" of the user
* Function Name: ChatResponse
* Function Purpose: once receive reply by chatBot, the response is stored temporarily
* in the data class ChatResponse
* Function Arguments: String chatBotReply
* Results:
*         Success: Send and Retrieve messages from user and ChatBot.
*         Failed:
*---------------------------------------------------
*/
interface APIService {
    @FormUrlEncoded
    @POST("chat")
    fun chatWithTheBot(@Field("chatInput") chatText : String ): Call<ChatResponse>
}

data class ChatResponse(val chatBotReply: String)