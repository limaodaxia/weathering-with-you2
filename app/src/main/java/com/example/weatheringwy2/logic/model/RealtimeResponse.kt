package com.example.weatheringwy2.logic.model

import com.google.gson.annotations.SerializedName

//realtime.json对应的类
data  class RealtimeResponse(val status:String, val result: Result){

    data class Result(val realtime: Realtime)

    data class  Realtime(val skycon:String, val temperature:Float, val wind: Wind, val visibility:Float,
                         @SerializedName("air_quality") val airQuality: AirQuality, val humidity:Float,
                         @SerializedName("apparent_temperature")val apparentTemp:Float, val pressure: Float)

    data class Wind(val direction:Float,val speed:Float)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn:Float)
}