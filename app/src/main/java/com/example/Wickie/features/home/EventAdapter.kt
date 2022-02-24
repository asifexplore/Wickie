package com.example.Wickie.features.home

//code cleanup
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R

class EventAdapter(private val eventList:ArrayList<Banner>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Log.d("Image_View", "Inside before ")
//        Log.d("Image_View", eventList[position].image.toString())
//        holder.imageView.setImageResource(eventList[position].image)
        holder.imageView.setImageResource(eventList[position].image)

    }

    override fun getItemCount() = eventList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.EventView1)
//        val textView: TextView = itemView.findViewById(R.id.title_text_view)
    }

}