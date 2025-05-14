package com.example.weatheringwy2.ui.place

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatheringwy2.databinding.ActivitySearchBinding
import com.example.weatheringwy2.ui.weather.WeatherActivity


class SearchActivity : AppCompatActivity() {
    //此时并没有赋值，而是第一次加载时候赋值
    val viewModel by lazy { ViewModelProvider(this).get(SearchPlaceViewModel::class.java) }

    private lateinit var adapter: SearchPlaceAdapter

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val source = intent.getStringExtra("from")
        if(viewModel.isSharedPreferencesPlaceSaved() && "MyPlaceFragment" != source){
            val place = viewModel.getSharedPreferencesPlace()
            //这里把我们存的place传送过去，要保证数据库中有他，同时也要保证删除的时候不能删除当前显示的
            val intent = Intent(this, WeatherActivity::class.java).apply {
                putExtra("place_data",place)
            }
            startActivity(intent)
            finish()
            return
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = SearchPlaceAdapter(this, viewModel.placeList)
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
        viewModel.placeLiveData.observe(this) { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }
}