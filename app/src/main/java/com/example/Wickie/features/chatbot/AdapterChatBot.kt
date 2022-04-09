package com.example.Wickie.features.chatbot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R
import kotlinx.android.synthetic.main.chat_item_list.view.*
/*
*  AdapterChatBot is the Adapter for the Wickie Fragment, the chat messages
*  uses recyclerView.
*
* Functions Within:
* ==========================================================================
* Function Name: AdapterChatBot: RecyclerView
* Function Purpose: retrieve and displays messages on the wickie fragment
* Function Arguments: Class (RecyclerView.Adapter)
* Results:
*         Success: Displays messages on wickie fragment
*         Failed:
*---------------------------------------------------
*/
class AdapterChatBot : RecyclerView.Adapter<AdapterChatBot.MyViewHolder>() {
    private val list = ArrayList<ChatModel>()
    /* relevant functions of the adapter:
    * OnCreateViewHolder, OnBindViewHolder and getItemCount
    * bind function in inner class MyViewHolder displays the chatModel object attributes
    * it takes the text of the object sent by the user and the Chatbot when
    * exchanging messages */
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
    /*
    * adds messages to a list and displays in the recyclerview */
    fun addChatToList(chat: ChatModel) {
        list.add(chat)
        notifyDataSetChanged()
    }

}