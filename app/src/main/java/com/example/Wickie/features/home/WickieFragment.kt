package com.example.Wickie.features.home
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.databinding.FragmentWickieBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Wickie.features.chatbot.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WickieFragment:Fragment()  {
    private val adapterChatBot = AdapterChatBot()
    private lateinit var binding : FragmentWickieBinding
    private lateinit var viewModel: ChatBotViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentWickieBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ChatBotViewModel::class.java)
        val apiService = viewModel.connect()
        binding.rvChatList.layoutManager = LinearLayoutManager(this.context)
        binding.rvChatList.adapter = adapterChatBot
        binding.btnSend.setOnClickListener {
            if(binding.etChat.text.isNullOrEmpty()){
                Toast.makeText(this.activity, "Please enter a text", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            adapterChatBot.addChatToList(ChatModel(binding.etChat.text.toString()))
            apiService.chatWithTheBot(binding.etChat.text.toString()).enqueue(callBack)
            onScrollPosition()
            binding.etChat.text.clear()
        }
        val root: View = binding.root
        return root
    }


    private val callBack = object  : Callback<ChatResponse>{
        override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
            if(response.isSuccessful &&  response.body()!= null){
                adapterChatBot.addChatToList(ChatModel(response.body()!!.chatBotReply, true))
            }else{
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
        }

    }
    private fun onScrollPosition() {
        binding.rvChatList.scrollToPosition(adapterChatBot.itemCount)
        binding.rvChatList.smoothScrollToPosition(adapterChatBot.itemCount)
        binding.rvChatList.layoutManager?.scrollToPosition(adapterChatBot.itemCount)
    }

}
