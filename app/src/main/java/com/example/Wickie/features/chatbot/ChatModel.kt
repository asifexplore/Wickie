package com.example.Wickie.features.chatbot
/*
*  ChatModel is the model object of the message by the User and ChatBot,
* it will be stored as a model and it contains a boolean to check whether it is from
* the user or ChatBoot
*
* Functions Within:
* ==========================================================================
* Function Name: ChatModel
* Function Arguments: String message(chat) and Boolean isBot(to check)
* Results:
*         Success: Converts Message into a ChatModel Object.
*         Failed:
*---------------------------------------------------
*/
data class ChatModel(val chat: String, val isBot: Boolean = false) {
}