package com.example.Wickie.features.chatbot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R
import kotlinx.android.synthetic.main.chat_item_list.view.*

class AdapterChatBot : RecyclerView.Adapter<AdapterChatBot.MyViewHolder>() {
    private val list = ArrayList<ChatModel>()

    inner class MyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
    ) {
        fun bind(chat: ChatModel) = with(itemView) {
            if(!chat.isBot) {
                txtChat.text = chat.chat
                txtBot.isVisible = false
            }else{
                txtChat.isVisible = false
                txtBot.text = chat.chat
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addChatToList(chat: ChatModel) {
        list.add(chat)
        notifyDataSetChanged()
    }

}