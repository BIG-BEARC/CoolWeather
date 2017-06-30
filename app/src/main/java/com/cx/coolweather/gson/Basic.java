package com.cx.coolweather.gson;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.gson
 *  @文件名:   Basic
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/29 15:19
 *  @描述：  基本信息
 */

import com.google.gson.annotations.SerializedName;

public class Basic {

    /**
     * city : 北京//城市名称
     * cnty : 中国 //国家
     * id : CN101010100//城市ID
     * lat : 39.904000 //城市维度
     * lon : 116.391000 //城市经度
     * update : {//更新时间
     * "loc":"2015-07-02 14:44", //当地时间
     * "utc":"2015-07-02 06:46"} //UTC时间
     */
    @SerializedName("city")
    public String     cityName;
    public String     cnty;
    @SerializedName("id")
    public String     weatherId;
    public String     lat;
    public String     lon;

    public UpdateBean update;

    public String getCityName() { return cityName;}

    public void setCityName(String cityName) { this.cityName = cityName;}

    public String getCnty() { return cnty;}

    public void setCnty(String cnty) { this.cnty = cnty;}

    public String getWeatherId() { return weatherId;}

    public void setWeatherId(String weatherId) { this.weatherId = weatherId;}

    public String getLat() { return lat;}

    public void setLat(String lat) { this.lat = lat;}

    public String getLon() { return lon;}

    public void setLon(String lon) { this.lon = lon;}

    public UpdateBean getUpdate() { return update;}

    public void setUpdate(UpdateBean update) { this.update = update;}

    public static class UpdateBean {
        /**
         * loc : 2015-07-02 14:44
         * utc : 2015-07-02 06:46
         */
        @SerializedName("loc")
        public String updateTimeLocal;
        @SerializedName("utc")
        public String updateTimeUTC;

        public String grtUpdateTimeLocal() { return updateTimeLocal;}

        public void setUpdateTimeLocal(String updateTimeLocal) { this.updateTimeLocal = updateTimeLocal;}

        public String getUpdateTimeUTC() { return updateTimeUTC;}

        public void setUpdateTimeUTC(String updateTimeUTC) { this.updateTimeUTC = updateTimeUTC;}
    }
}
