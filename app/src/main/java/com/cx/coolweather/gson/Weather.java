package com.cx.coolweather.gson;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.gson
 *  @文件名:   Weather
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/29 16:22
 *  @描述：    TODO
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    public AQI            aqi;
    public Basic          basic;
    public Now            now;
    public String         status;
    public Suggestion     suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;


}
