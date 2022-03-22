package com.example.Wickie.data.source

import android.widget.Toast
import com.example.Wickie.features.chatbot.APIService
import com.example.Wickie.features.chatbot.ChatModel
import com.example.Wickie.features.chatbot.ChatResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.Wickie.features.chatbot.*


class ChatbotRepository {
    private var adapterChatBot = AdapterChatBot()

    fun connect(): APIService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.2.186:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(APIService::class.java)
    }

    fun receive(): Callback<ChatResponse> {
        val callBack = object  : Callback<ChatResponse>{
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if(response.isSuccessful &&  response.body()!= null){
                    adapterChatBot.addChatToList(ChatModel(response.body()!!.chatBotReply, true))
                }else{
//                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
//            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        }
        return callBack
    }



}