package com.example.weatheringwy2.logic.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatheringwy2.logic.dao.PlaceDao
import com.example.weatheringwy2.logic.model.Place

//Room框架创建的数据库
@Database(version = 1,entities = [Place::class], exportSchema = false)
abstract class MyPlaceDatabase:RoomDatabase(){

    abstract fun placeDao(): PlaceDao

    companion object{
        private var instance: MyPlaceDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): MyPlaceDatabase {
            instance?.let {
                return  it
            }
            return Room.databaseBuilder(context.applicationContext,
                MyPlaceDatabase::class.java,"my_place_database")
                .allowMainThreadQueries()
                .build().apply {
                    instance = this
                }
        }
    }
}