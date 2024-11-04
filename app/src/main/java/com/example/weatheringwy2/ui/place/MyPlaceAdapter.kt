package com.example.weatheringwy2.ui.place

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheringwy2.R
import com.example.weatheringwy2.logic.model.Place
import com.example.weatheringwy2.ui.weather.WeatherActivity


class MyPlaceAdapter (private val fragment: MyPlaceFragment, private val myPlaceList:List<Place>)
    :RecyclerView.Adapter<MyPlaceAdapter.ViewHolder>(){


    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val placeName :TextView = view.findViewById(R.id.placeName)
        val placeAddress :TextView = view.findViewById(R.id.placeAddress)
        val removeBtn :TextView = view.findViewById(R.id.itemBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder= ViewHolder(view)

        view.setOnClickListener {
            val position = holder.adapterPosition
            val place = myPlaceList[position]
            val activity = fragment.activity as WeatherActivity

            activity.closeDrawer()
            activity.changePage(position)

            //这里是要转换成我们的fragment的原因，因为我们的fragment中有ViewModel
            fragment.viewModel.saveSharedPreferencesPlace(place)//保存我们新的地址到数据
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //先加载基本信息
        val place = myPlaceList[position]
        holder.placeAddress.text = place.address
        holder.placeName.text = place.name
        //如果当前页是我们加入的那一页，我们就
        if (fragment.viewModel.getSharedPreferencesPlace().id == place.id){
            holder.removeBtn.setTextColor(Color.GRAY)
            holder.removeBtn.text = "当前页"
            holder.removeBtn.isClickable = false
        }else {
            holder.removeBtn.setTextColor(Color.BLACK)
            holder.removeBtn.text = "移除"
            holder.removeBtn.setOnClickListener {
                fragment.viewModel.deleteMyPlace(place)
            }
        }
    }

    override fun getItemCount() = myPlaceList.size

}