package com.example.weatheringwy2.logic.model

import com.google.gson.annotations.SerializedName

//对应于每分钟的来写的minutely.json，借此来获得天气建议
data class MinutelyResponse (val status:String, val result: Result){
    data class Result(@SerializedName("forecast_keypoint") val forecastKeypoint: String)
}


