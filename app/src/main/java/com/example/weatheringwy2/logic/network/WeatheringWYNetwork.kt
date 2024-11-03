package com.example.weatheringwy2.logic.network

import com.example.weatheringwy2.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object WeatheringWYNetwork {

    private val  placeService = ServiceCreator.create<PlaceService>()//创建了一个动态代理对象

    /*await把call<PlaceResponse>得到的Response<变成PlaceResponse>变成PlaceResponse*/
    suspend fun searchPlaces(query:String): PlaceResponse = placeService.searchPlace(query).await()

    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun getDailyWeather(lng:String,lat:String) = weatherService.getDailyWeather(lng,lat) .await()

    suspend fun getRealtimeWeather(lng:String,lat:String) = weatherService.getRealtimeWeather(lng,lat) .await()

    suspend fun getMinutelyRainfall(lng: String,lat: String) = weatherService.getMinutelyRainfall(lng,lat).await()

    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}