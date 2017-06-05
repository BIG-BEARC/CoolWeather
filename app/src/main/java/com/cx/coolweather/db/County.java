package com.cx.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * @项目名： CoolWeather
 * @包名： com.cx.coolweather.db
 * @文件名: County
 * @创建者: Administrator
 * @创建时间: 2017/6/5 23:02
 * @描述： TODO
 */

public class County extends DataSupport {
    private int id;
    private int cityId;//当前所属市的id
    private String countyName;//县名
    private String weatherId;//县对应的天气id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
