package com.example.weatheringwy2.logic.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//对应于place接口写的类，同时这也是数据库的表，所以加上实体标志
data class PlaceResponse(val status:String, val places:List<Place>)

@Entity
data class Place(var name:String, @Embedded val location: Location, @PrimaryKey val id:String,
                 @SerializedName("formatted_address")/*这个能把json字段与kotlin字段建立映射,以便我们的转换*/ val address:String)
    :Serializable
data class Location(val lng:String, val lat:String):Serializable