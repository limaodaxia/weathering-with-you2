package com.example.weatheringwy2.logic.network

import com.example.weatheringwy2.WeatheringWY2Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//创建网络服务的工具类，指定了服务器的地址，同时也
object ServiceCreator {
//    val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            val originalRequest = chain.request()
//            val newRequest = originalRequest.newBuilder()
//                .url(originalRequest.url().newBuilder()
//                    .addQueryParameter("token", WeatheringWY2Application.TOKEN)
//                    .addQueryParameter("lang", "zh_CN")
//                    .build())
//                .build()
//            chain.proceed(newRequest)
//        }
//        .build()

    private const val BASE_URL = "https://api.caiyunapp.com"//指定服务器的根地址
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.client(okHttpClient)//指定我们的拦截器
        .addConverterFactory(GsonConverterFactory.create())//指定我们转换JSON的方法
        .build()

    /*这里有类型推导,所以不用给方法指定类型了,只需要传入参数,泛型也就指定了*/
    fun <T> create(serviceClass:Class<T>):T = retrofit.create(serviceClass)

    // 这里使用了泛型实化为的是可以使用 T::class.java的语法了，我们传入的是各种Service的接口
    inline fun <reified T> create() = create(T::class.java)
}