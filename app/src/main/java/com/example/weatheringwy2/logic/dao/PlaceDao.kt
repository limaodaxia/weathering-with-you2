package com.example.weatheringwy2.logic.dao

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weatheringwy2.WeatheringWY2Application
import com.google.gson.Gson
import com.example.weatheringwy2.logic.model.Place
@Dao
interface PlaceDao {
    //数据库
    @Insert
    fun insertMyPlace(place: Place)

    @Query("select * from Place")
    fun loadAllMyPlace():LiveData<List<Place>>

    //@Query("select 1 from Place where id =:id  limit 1")
    @Query("select count(*) from Place where id = :id")
    fun isContainPlace(id:String):Int

    @Delete
    fun deleteMyPlace(place: Place)

    //sharedPreference
    companion object{
        fun savePlace(place: Place){
            sharedPreferences().edit {
                putString("place",Gson().toJson(place))
            }
        }

        fun getSavedPlace(): Place {
            val placeJson =  sharedPreferences().getString("place","")
            return Gson().fromJson(placeJson, Place::class.java)
        }

        fun isPlaceSaved() = sharedPreferences().contains("place")

        private  fun sharedPreferences() = WeatheringWY2Application.context
                .getSharedPreferences("weathering_with_you"/*指定存放的文件名*/,Context.MODE_PRIVATE)

    }
}