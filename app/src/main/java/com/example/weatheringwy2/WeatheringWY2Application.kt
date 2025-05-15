package com.example.weatheringwy2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.media.session.MediaSession.Token

class WeatheringWY2Application :Application(){
    companion object{
//        // 使用 var 而非 const，因为值在运行时读取
//        var TOKEN: String? = null
//            private set
        const val TOKEN = "t45mNJXzowjH9XJA"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context;
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //loadTokenFromFile()
    }

//    private fun loadTokenFromFile() {
//        try {
//            // 从 assets 中读取 token.txt
//            val inputStream = context.assets.open("token.txt")
//            val size = inputStream.available()
//            val buffer = ByteArray(size)
//            inputStream.read(buffer)
//            inputStream.close()
//            val token = String(buffer, Charsets.UTF_8).trim()
//            TOKEN = token
//        } catch (e: Exception) {
//            // 可选：记录日志或设置默认值
//            e.printStackTrace()
//            TOKEN = "default_token"
//        }
//    }
}