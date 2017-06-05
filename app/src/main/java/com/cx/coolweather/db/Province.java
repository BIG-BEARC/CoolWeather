package com.cx.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * @项目名： CoolWeather
 * @包名： com.cx.coolweather.db
 * @文件名: Province
 * @创建者: Administrator
 * @创建时间: 2017/6/5 22:45
 * @描述： 省数据
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;//省名
    private  int provinceCode;//省代号


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
