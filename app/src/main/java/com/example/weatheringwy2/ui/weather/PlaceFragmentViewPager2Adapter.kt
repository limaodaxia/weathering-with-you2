package com.example.weatheringwy2.ui.weather

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatheringwy2.logic.model.Place


//这是viewpager2对应于fragment的监听器，每一个fragment对应于一个地点，所以我们要把placeList
//其中的每一个place对应着我们的一个fragment，我们创建的fragment也是我们自己手写的
class PlaceFragmentViewPager2Adapter(var placeFragmentList: List<Place>, activity: WeatherActivity)
    :FragmentStateAdapter(activity) {

    val idSet = mutableSetOf<Long>()

    override fun getItemCount(): Int {
        return placeFragmentList.size
    }

    override fun createFragment(position: Int): WeatherFragment {
        return WeatherFragment(placeFragmentList[position])
    }

    override fun getItemId(position: Int): Long {
        val id = placeFragmentList[position].hashCode().toLong()
        idSet.add(id)
        return id // 或基于fragmentList[position].id等唯一标识
    }

    // 判断指定ID的Fragment是否存在于当前列表中
    override fun containsItem(itemId: Long): Boolean {
        return idSet.contains(itemId)
    }
}
