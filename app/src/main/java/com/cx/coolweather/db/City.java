package com.cx.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * @项目名： CoolWeather
 * @包名： com.cx.coolweather.db
 * @文件名: City
 * @创建者: Administrator
 * @创建时间: 2017/6/5 22:59
 * @描述： TODO
 */

public class City extends DataSupport {
    private int id;
    private String cityName;//市名字
    private int cityCode;//市代号
    private int provinceId;//记录当前市所属省的id值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}