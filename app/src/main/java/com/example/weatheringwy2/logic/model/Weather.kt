package com.example.weatheringwy2.logic.model

import com.example.weatheringwy2.logic.model.DailyResponse
import com.example.weatheringwy2.logic.model.RealtimeResponse

//把分钟级，日级，实时的天气组合成weather，我们利用跟这个来构建天气信息图
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily, val minutely: MinutelyResponse.Result)