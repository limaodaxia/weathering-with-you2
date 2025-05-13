package com.example.weatheringwy2.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatheringwy2.MainActivity
import com.example.weatheringwy2.databinding.FragmentPlaceBinding
import com.example.weatheringwy2.ui.weather.WeatherActivity



class SearchPlaceFragment: Fragment() {

    //此时并没有赋值，而是第一次加载时候赋值
    val viewModel by lazy { ViewModelProvider(this).get(SearchPlaceViewModel::class.java) }

    private lateinit var adapter: SearchPlaceAdapter

    private lateinit var binding:FragmentPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if( activity is MainActivity && viewModel.isSharedPreferencesPlaceSaved()){
            val place = viewModel.getSharedPreferencesPlace()
            //这里把我们存的place传送过去，要保证数据库中有他，同时也要保证删除的时候不能删除当前显示的
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("place_data",place)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = SearchPlaceAdapter(this,viewModel.placeList)
        binding.recyclerView.adapter = adapter

        binding.searchPlaceEdit.addTextChangedListener{
            editable ->
            val content = editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        /*observe里用placeLiveData的Result<List<Place>>*/
        viewModel.placeLiveData.observe(viewLifecycleOwner) { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }
}