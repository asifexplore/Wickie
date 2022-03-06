package com.example.Wickie.features.home

//code cleanup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R
import com.example.Wickie.data.source.data.Claim

class ClaimAdapter(private val claimList:ArrayList<Claim>) : RecyclerView.Adapter<ClaimAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.claims_fragment_cell,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTxtView.text = claimList[position].title
        holder.dateTxtView.text = claimList[position].claimDate
        holder.amountTxtView.text = "$"+claimList[position].amount.toString()
        holder.statusTxtView.text = claimList[position].status

        if (claimList[position].type == "transport")
        {
            holder.typeImgView.setImageResource(R.drawable.ic_transport_foreground)
        }else
        {
            holder.typeImgView.setImageResource(R.drawable.ic_transport_foreground)
        }

    }

    override fun getItemCount() = claimList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxtView: TextView = itemView.findViewById(R.id.claimTitle)
        val dateTxtView : TextView = itemView.findViewById(R.id.claimDate)
        val amountTxtView : TextView = itemView.findViewById(R.id.claimAmt)
        val statusTxtView : TextView = itemView.findViewById(R.id.claimStatus)
        val typeImgView : ImageView = itemView.findViewById(R.id.claimImageType)

    }

}