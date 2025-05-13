package com.example.weatheringwy2.ui.weather

import androidx.viewpager2.adapter.FragmentStateAdapter


//这是viewpager2对应于fragment的监听器，每一个fragment对应于一个地点，所以我们要把placeList
//其中的每一个place对应着我们的一个fragment，我们创建的fragment也是我们自己手写的
class PlaceFragmentViewPager2Adapter(val placeFragmentList: List<WeatherFragment>,
                                     activity: WeatherActivity, )
    :FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return placeFragmentList.size
    }

    override fun createFragment(position: Int): WeatherFragment {
        return placeFragmentList[position]
    }


}
