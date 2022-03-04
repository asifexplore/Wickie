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

class ClaimAdapter(private val claimList:ArrayList<Claim>) : RecyclerView.Adapter<ClaimAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.claims_history,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Log.d("Image_View", "Inside before ")
//        Log.d("Image_View", eventList[position].image.toString())
//        holder.imageView.setImageResource(eventList[position].image)
        holder.tvName.text = claimList[position].name
        holder.tvDate.text = claimList[position].date
        holder.status.text = claimList[position].status
        holder.price.text = claimList[position].price.toString()
        holder.ivType.setImageResource(claimList[position].image)
    }

    override fun getItemCount() = claimList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.findViewById(R.id.tvName)
        val tvDate : TextView = itemView.findViewById(R.id.tvDate)
        val status : TextView = itemView.findViewById(R.id.claim_status)
        val price : TextView = itemView.findViewById(R.id.claim_price)
        val ivType : ImageView = itemView.findViewById(R.id.ivType)
//        val textView: TextView = itemView.findViewById(R.id.title_text_view)
    }

}