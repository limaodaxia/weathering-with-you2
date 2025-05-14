package com.example.weatheringwy2.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheringwy2.databinding.PlaceItemBinding
import com.example.weatheringwy2.logic.Repository
import com.example.weatheringwy2.logic.model.Place
import com.example.weatheringwy2.ui.weather.WeatherActivity


class SearchPlaceAdapter(private val activity: SearchActivity, private val placeList: List<Place>):RecyclerView.Adapter<SearchPlaceAdapter.ViewHolder>() {

    private lateinit var binding: PlaceItemBinding

    inner class ViewHolder(binding:PlaceItemBinding):RecyclerView.ViewHolder(binding.root){
        val placeName :TextView= binding.placeName
        val placeAddress :TextView= binding.placeAddress
        val addBtn :Button = binding.itemBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ViewHolder(binding)

        // 第一次的时候不显示添加按钮
        if (activity.viewModel.isSharedPreferencesPlaceSaved()){
            holder.addBtn.visibility = View.GONE
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
                val place = placeList[position]
                val intent = Intent(parent.context, WeatherActivity::class.java)
                Repository.insertMyPlace(place)
                activity.viewModel.saveSharedPreferencesPlace(place)
                //原来我们之前创建的参数这里用到了
                activity.startActivity(intent)
//               fragment.activity?.finish()//这里把mainActivity关掉了，同时fragment也没了
                activity.finish()//这里把mainActivity关掉了，因为上面获取过了activity，所以现在这里不需要在获取，直接调用

            }

        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address

        val exist = Repository.isContainPlace(placeList[position].id)==1
        if (exist){
            holder.addBtn.isClickable = false
            holder.addBtn.text = "已添加"
            Log.d("test", "name${holder.placeName.text}")
        }else{
            holder.addBtn.text = "添加"
            holder.addBtn.setOnClickListener {
                Repository.insertMyPlace(place)
                holder.addBtn.isClickable = false
                holder.addBtn.text = "已添加"
            }

        }

    }

    override fun getItemCount() = placeList.size

    override fun getItemViewType(position: Int): Int {
        return Repository.isContainPlace(placeList[position].id)
    }
}