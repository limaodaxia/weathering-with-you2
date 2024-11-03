package com.example.weatheringwy2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeatheringWY2Application :Application(){
    companion object{
        const val TOKEN = "t45mNJXzowjH9XJA"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context;
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}