package com.example.weatheringwy2.logic


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.weatheringwy2.WeatheringWY2Application
import com.example.weatheringwy2.logic.dao.MyPlaceDatabase
import com.example.weatheringwy2.logic.dao.PlaceDao
import com.example.weatheringwy2.logic.model.Place
import com.example.weatheringwy2.logic.model.Weather
import com.example.weatheringwy2.logic.network.WeatheringWYNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

object Repository {

    //数据库
    private val placeDao = MyPlaceDatabase.getDatabase(WeatheringWY2Application.context).placeDao()

    private val myPlaceLiveData:LiveData<List<Place>> = placeDao.loadAllMyPlace()

    fun loadAllMyPlaces():LiveData<List<Place>>{
        return myPlaceLiveData
    }

    fun insertMyPlace(place: Place){
        thread {
            placeDao.insertMyPlace(place)
        }
    }

    fun isContainPlace(id:String):Int{
        return placeDao.isContainPlace(id) //返回拥有数
    }

    fun deleteMyPlace(place: Place){
        thread {
            placeDao.deleteMyPlace(place)
        }
    }

    //sharedPreference
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()



    //网络端
    fun searchPlaces(query:String) = fire(Dispatchers.IO){

        val placeResponse = WeatheringWYNetwork.searchPlaces(query)

        if (placeResponse.status == "ok"){
            val places = placeResponse.places
            Result.success(places)
            
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }

    }

    fun refreshWeather(lng: String,lat: String) = fire(Dispatchers.IO/*这个线程适合网络通信*/) {
        //coroutineScope函数继承外部协程的作用域并且创建一个子协程，保证其作用域内的代码和子协程全部执行完之前外部协程被挂起
        //也就是说只有当其中程序执行完了，这个函数之外的代码才能得到运行

        coroutineScope {
            val deferredRealtime = async {
                WeatheringWYNetwork.getDailyWeather(lng,lat)
            }
            val deferredDaily = async {
                WeatheringWYNetwork.getRealtimeWeather(lng,lat)
            }
            val deferredMinutelyRainfall =async {
                WeatheringWYNetwork.getMinutelyRainfall(lng,lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            val minutelyResponse = deferredMinutelyRainfall.await()
            Log.d("testa", "${minutelyResponse.toString()}")

            if(realtimeResponse.status == "ok" && dailyResponse.status == "ok" &&
                minutelyResponse.status == "ok"){
                Log.d("testa","3 response is ok")
                val weather = Weather(dailyResponse.result.realtime,
                    realtimeResponse.result.daily,minutelyResponse.result)
                Log.d("testa",weather.toString())
                Result.success(weather)
            }else{
                Result.failure(
                        RuntimeException(
                                "realtime response status is ${realtimeResponse.status}"+
                                        "daily response status is ${dailyResponse.status}"+
                                        "daily response status is ${minutelyResponse.status}"
                        )
                )
            }

        }
    }

    private fun <T> fire(context:CoroutineContext, block:suspend ()->Result<T>) = liveData<Result<T>> {
        val result=try {
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}