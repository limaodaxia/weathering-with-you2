package com.example.weatheringwy2.logic.model

//风速转换成风级
fun getWindLevel(speed:Float):String{
    val mPs = speed/3600*1000
    return when{
        mPs<=1.5 ->"1级"
        mPs<=3.3 ->"2级"
        mPs<=5.4 ->"3级"
        mPs<=7.9 ->"4级"
        mPs<=10.4 ->"5级"
        mPs<=13.8 ->"6级"
        mPs<=17.1 ->"7级"
        mPs<=20.7 ->"8级"
        mPs<=24.4 ->"9级"
        mPs<=28.4 ->"10级"
        mPs<=32.6 ->"11级"
        mPs<=36.9 ->"12级"
        mPs<=41.4 ->"13级"
        mPs<=46.1 ->"14级"
        mPs<=50.9 ->"15级"
        mPs<=56.0 ->"16级"
        mPs<=61.2 ->"17级"
        mPs<=61.3 ->"18级"
        mPs<=69.4 ->"19级"
        else ->"爆表了"
    }



}
//角度转换成风向
fun getWindDirection(direction:Float):String{
    return when{
        direction<=22.5->"北风"
        direction<=67.5 ->"东北风"
        direction<=112.5 ->"东风"
        direction<=157.5 ->"东南风"
        direction<=202.5 ->"南风"
        direction<=247.5 ->"西南风"
        direction<=292.5 ->"西风"
        direction<=337.5 ->"西北风"
        direction<=360 ->"北风"
        else -> "unknown"
    }
}