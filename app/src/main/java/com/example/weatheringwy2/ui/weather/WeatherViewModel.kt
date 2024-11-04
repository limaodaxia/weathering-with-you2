package com.example.weatheringwy2.ui.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.weatheringwy2.logic.Repository
import com.example.weatheringwy2.logic.model.Location
import kotlin.reflect.typeOf

//现在把其指定给WeatherFragment了
class WeatherViewModel:ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    // 这只是存数据
    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    val weatherLiveData /**/ = locationLiveData.switchMap{
        location ->
            val t = Repository.refreshWeather(location.lng,location.lat)
            Log.d("test", "${t.value}${Thread.currentThread().id}")
            t
    }

    fun refreshWeather(lng:String,lat:String){
        locationLiveData.value = Location(lng,lat)
    }
}