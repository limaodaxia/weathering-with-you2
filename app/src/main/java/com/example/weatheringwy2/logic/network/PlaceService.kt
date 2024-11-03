package com.example.weatheringwy2.logic.network



import com.example.weatheringwy2.WeatheringWY2Application
import com.example.weatheringwy2.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//place相关的网络服务请求都定义在这里，调用接口对应发送一个请求
interface PlaceService {
    @GET("v2/place?=token=${WeatheringWY2Application.TOKEN}&lang=zh_CN")//书P603页的搜索城市的API
    fun searchPlace(@Query("query")/*会把这个参数按照get的方式附加到URL后*/ query:String) : Call<PlaceResponse>//json转换成PlaceResponse
}