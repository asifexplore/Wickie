package com.example.Wickie.features.home
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Wickie.R
import com.example.Wickie.databinding.FragmentHomeBinding


class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.recyclerViewEventList.layoutManager = LinearLayoutManager(this.context)

        var arrBanner: ArrayList<Banner> = ArrayList()
        arrBanner.add(Banner("event_banner1.jpg",R.drawable.event_banner1))
        var adapter = EventAdapter(arrBanner)
        binding.recyclerViewEventList.adapter = adapter

    }

}
