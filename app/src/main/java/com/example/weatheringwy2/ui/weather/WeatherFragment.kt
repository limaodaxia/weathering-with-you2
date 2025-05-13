package com.example.weatheringwy2.ui.weather


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatheringwy2.logic.model.Place
import com.example.weatheringwy2.logic.model.Weather
import com.example.weatheringwy2.logic.model.getSky
import com.example.weatheringwy2.logic.model.getWindDirection
import com.example.weatheringwy2.logic.model.getWindLevel
import com.example.weatheringwy2.R
import com.example.weatheringwy2.WeatheringWY2Application
import com.example.weatheringwy2.databinding.FragmentWeatherBinding
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer

class WeatherFragment(val place: Place):Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    private lateinit var binding: FragmentWeatherBinding

    private var isToast = false //第一次不用Toast，之后都要

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //看看当前这个ViewModel有没有东西，如果没有

        val weatherActivity = activity as WeatherActivity
        viewModel.locationLng = place.location.lng
        viewModel.locationLat = place.location.lat
        viewModel.placeName = place.name

        //观察weatherLiveData已有变化
        viewModel.weatherLiveData.observe(viewLifecycleOwner) /*这里可能不是activity androidx.lifecycle.Observer*/ { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                //更新页面状态
                showWeatherInfo(weather)
                //如果当前是正在刷新，提示刷新完成
                if (binding.swipeRefresh.isRefreshing) {
                    Toast.makeText(WeatheringWY2Application.context, "刷新完成", Toast.LENGTH_SHORT)
                        .show()
                } else if (isToast) {
                    showTip(weather)
                }
            } else {
                Toast.makeText(
                    WeatheringWY2Application.context,
                    "无法成功获取天气等信息",
                    Toast.LENGTH_SHORT
                ).show()
                //result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing = false
            weatherActivity.changeUserInputEnabled(true)

        }

        //设置一下刷新的颜色
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)

        //当我们做出刷新手势的时候去执行刷新操作
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        //onTouch lambda should call view#performclick when a click is detected
        //设置刷新的时候,无法滑动viewpager
        binding.swipeRefresh.setOnTouchListener { v, event ->
            weatherActivity.changeUserInputEnabled(false)
            false
            //这里不能返回true
            //当return返回值为true的时候，代表这个事件已经消耗完了，返回值为false的时候他还会继续传递
            //如果我们返回true，会导致时间结束，那后面的刷新等等事件就不会处理这个时间了
        }

        //设置点击朱强现实的按钮
        binding.lifeIndex.lxlIcon.setOnClickListener {
            getInfoWithToast()
        }

        //设置导航栏上面的按钮
        binding.nowTop.navBtn.setOnClickListener {
            weatherActivity.openDrawer() //打开我们左边的菜单
        }

        //所有都设置完了智慧，获取结果
        getInfo()

    }

    //从网络接口获取信息，本质上是去通知一下location(改了,实际上没改)，然后进而去网上请求一下接口，转换成可观察的weather
    private fun getInfo(){
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    // 这里想在刷新的同时展示一下tip
    private fun getInfoWithToast(){
        isToast = true
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    //刷新操作，再获取信息的技术上
    private fun refreshWeather(){
        getInfo()
        binding.swipeRefresh.isRefreshing = true
    }

    //显示当前的获得的生活指数的描述
    private fun showTip(weather: Weather){
        val tip = weather.minutely.forecastKeypoint
        Toast.makeText(WeatheringWY2Application.context/*获得一下当前*/, tip, Toast.LENGTH_SHORT).show()
    }

    //更新页面状态，简单的设置布局内容
    private fun showWeatherInfo(weather: Weather){
        //地址信息
        binding.nowTop.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        //填充now.xml布局中的数据,三个关于天气的text
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        binding.nowTop.currentTemp.text = currentTempText
        binding.nowTop.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        binding.nowTop.currentAQI.text = currentPM25Text
        binding.nowTop.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        //填充now_detail.xml
        binding.nowDetail.richuText.text = "日出 ${weather.daily.astro[0].sunrise.time}"
        binding.nowDetail.riluoText.text = "日落 ${weather.daily.astro[0].sunset.time}"
        binding.nowDetail.humidity.text = "${(weather.realtime.humidity*100).toInt()}%"
        binding.nowDetail.windLevel.text = getWindLevel(weather.realtime.wind.speed)
        binding.nowDetail.windDirection.text = getWindDirection(weather.realtime.wind.direction)
        binding.nowDetail.bodyTemp.text = "${weather.realtime.apparentTemp.toInt()}°"
        binding.nowDetail.pressure.text = "${(weather.realtime.pressure/100).toInt()}hPa"
        //填充forecast.xml的信息
        binding.forecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            //这里我们采取一个一个加上view而不是用列表，因为我们这是不可变的，不需要用ListView来搞各种事件
            val view = LayoutInflater.from(WeatheringWY2Application.context/*获得一下系统的*/).inflate(R.layout.forecast_item,
                binding.forecast.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dataInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            when (i) {
                0 -> {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd ", Locale.CHINA)
                    dateInfo.text = simpleDateFormat.format(skycon.date) + "今天"
                }
                1 -> {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd ", Locale.CHINA)
                    dateInfo.text = simpleDateFormat.format(skycon.date) + "明天"
                }
                else -> {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd E", Locale.CHINA)
                    dateInfo.text = simpleDateFormat.format(skycon.date)
                }
            }
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            binding.forecast.forecastLayout.addView(view)
        }
        //填充life_index.xml
        val lifeIndex = daily.lifeIndex
        binding.lifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.lifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.lifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.lifeIndex.visibility.text = "${weather.realtime.visibility}km"
        binding.weatherLayout.visibility = View.VISIBLE
    }

}