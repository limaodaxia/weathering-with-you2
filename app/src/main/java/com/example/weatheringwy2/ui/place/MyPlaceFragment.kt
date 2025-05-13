package com.example.weatheringwy2.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatheringwy2.R
import com.example.weatheringwy2.databinding.ActivityMainBinding
import com.example.weatheringwy2.databinding.FragmentMyPlaceBinding
import com.example.weatheringwy2.ui.weather.WeatherActivity


class MyPlaceFragment:Fragment() {

    private lateinit var binding: FragmentMyPlaceBinding
    //此时并没有赋值，而是第一次加载时候赋值
    val viewModel by lazy { ViewModelProvider(this).get(MyPlaceViewModel::class.java) }

    private lateinit var adapter: MyPlaceAdapter

    fun getAdapter() = adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = MyPlaceAdapter(this,viewModel.myPlaceList)
        binding.recyclerView.adapter = adapter

        binding.addPlaceButton.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        viewModel.loadAllMyPlaces().observe(viewLifecycleOwner/*这里换一下*/, Observer { places->
            if(places!=null){
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.myPlaceList.clear()
                viewModel.myPlaceList.addAll(places)
                adapter.notifyDataSetChanged()
                val c = activity as WeatherActivity
                c.viewModel.refresh()
            }
        })
    }
}