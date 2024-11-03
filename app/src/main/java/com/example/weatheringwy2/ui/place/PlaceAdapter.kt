package com.example.weatheringwy2.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheringwy2.MainActivity
import com.example.weatheringwy2.R
import com.example.weatheringwy2.databinding.ActivityMainBinding
import com.example.weatheringwy2.logic.Repository
import com.example.weatheringwy2.logic.model.Place
import com.example.weatheringwy2.ui.weather.WeatherActivity


class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private lateinit var binding: ActivityMainBinding

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val placeName :TextView= view.findViewById(R.id.placeName)
        val placeAddress :TextView= view.findViewById(R.id.placeAddress)
        val addBtn :Button = view.findViewById(R.id.itemBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder = ViewHolder(view)
        val activity = fragment.activity
        if (activity is MainActivity){
            holder.addBtn.visibility = View.GONE
            view.setOnClickListener {
                val position = holder.adapterPosition
                val place = placeList[position]
                val intent = Intent(parent.context, WeatherActivity::class.java)
                Repository.insertMyPlace(place)
                fragment.viewModel.savePlace(place)
                //原来我们之前创建的参数这里用到了
                fragment.startActivity(intent)
//               fragment.activity?.finish()//这里把mainActivity关掉了，同时fragment也没了
                activity?.finish()//这里把mainActivity关掉了，因为上面获取过了activity，所以现在这里不需要在获取，直接调用

            }

        }else{//SearchActivity
            if (viewType==1){
                holder.addBtn.isClickable = false
                holder.addBtn.text = "已添加"
            }else{
                holder.addBtn.text = "添加"
                holder.addBtn.setOnClickListener {
                    val position = holder.adapterPosition
                    val place = placeList[position]
                    Repository.insertMyPlace(place)
                    holder.addBtn.isClickable = false
                    holder.addBtn.text = "已添加"
                }

            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size

    override fun getItemViewType(position: Int): Int {
        return Repository.isContainPlace(placeList[position].id)
    }
}