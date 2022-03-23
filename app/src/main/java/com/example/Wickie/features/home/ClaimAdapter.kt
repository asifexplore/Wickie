package com.example.Wickie.features.home

//code cleanup
import android.app.ProgressDialog.show
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.Wickie.R
import com.example.Wickie.data.source.data.Claim
import com.example.Wickie.features.claims.ClaimViewModel
import com.example.Wickie.features.claims.ClaimsDetailsActivity

class ClaimAdapter(
    private val claimList:ArrayList<Claim>
    ) : RecyclerView.Adapter<ClaimAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.claims_fragment_cell,parent,false)
        return ViewHolder(view)
    }

    var viewModel: ClaimViewModel = ClaimViewModel()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTxtView.text = claimList[position].title
        holder.dateTxtView.text = claimList[position].claimDate
        holder.amountTxtView.text = "$"+claimList[position].amount.toString()
        holder.statusTxtView.text = claimList[position].status

        if (claimList[position].type == "transport")
        {
            holder.typeImgView.setImageResource(R.drawable.transport_icon)
        }else if (claimList[position].type == "phone")
        {
            holder.typeImgView.setImageResource(R.drawable.phone_bill)
        }else{
            holder.typeImgView.setImageResource(R.drawable.meal_icon)
        }

        holder.itemView.setOnClickListener{
            viewModel.setChosenClaim(position)
            // Intent
            holder.itemView.setOnClickListener { v ->
                val intent2 = Intent(v.context, ClaimsDetailsActivity::class.java)
                    .putExtra("title",claimList[position].title)
                    .putExtra("claimDate",claimList[position].claimDate.toString())
                    .putExtra("amount",claimList[position].amount.toString())
                    .putExtra("type",claimList[position].type.toString())
                    .putExtra("status",claimList[position].status.toString())
                    .putExtra("imgUrl",claimList[position].imageUrl)
                    .putExtra("claimID",claimList[position].claimID)
                    .putExtra("claimObj",claimList[position])

                Log.d("ClaimAdapter", claimList[position].imageUrl.toString())
                v.context.startActivity(intent2)

            }
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