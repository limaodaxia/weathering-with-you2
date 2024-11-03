package com.example.weatheringwy2.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*
//对应于daily.json返回的接口格式而写的
data class DailyResponse(val status:String,val result: Result){

    data class Result(val daily: Daily)

    data class Daily(val temperature:List<Temperature>, val skycon :List<Skycon>
                     , @SerializedName("life_index") val lifeIndex: LifeIndex, val astro :List<Astro>)

    data class Temperature(val max:Float,val min:Float)

    data class Astro(val sunrise: Sunrise, val sunset: Sunset)

    data class Sunrise(val time:String)

    data class Sunset(val time:String)

    data class Skycon(val value:String,val date:Date)

    data class LifeIndex(val coldRisk:List<LifeDescription>, val carWashing:List<LifeDescription>,
                         val ultraviolet:List<LifeDescription>, val dressing:List<LifeDescription>)

    data class LifeDescription(val desc:String)
}