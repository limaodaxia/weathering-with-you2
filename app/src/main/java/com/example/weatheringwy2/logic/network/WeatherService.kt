package com.example.weatheringwy2.logic.network


import com.example.weatheringwy2.WeatheringWY2Application
import com.example.weatheringwy2.logic.model.DailyResponse
import com.example.weatheringwy2.logic.model.MinutelyResponse
import com.example.weatheringwy2.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
//weather相关的网络服务请求都定义在这里，调用接口对应发送一个请求
interface WeatherService {
    @GET("v2.5/${WeatheringWY2Application.TOKEN}/{lng},{lat}/realtime")
    fun getRealtimeWeather(@Path("lng") lng:String,@Path("lat") lat:String):Call<RealtimeResponse>

    @GET("v2.5/${WeatheringWY2Application.TOKEN}/{lng},{lat}/daily")
    fun getDailyWeather(@Path("lng") lng:String,@Path("lat") lat:String):Call<DailyResponse>

    @GET("v2.6/${WeatheringWY2Application.TOKEN}/{lng},{lat}/weather")
    fun getMinutelyRainfall(@Path("lng")  lng:String, @Path("lat") lat:String):Call<MinutelyResponse>
}