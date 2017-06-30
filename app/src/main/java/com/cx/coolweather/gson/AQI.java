package com.cx.coolweather.gson;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.gson
 *  @文件名:   AQI
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/29 15:35
 *  @描述：   //空气质量，仅限国内城市
 */

public class AQI {

    /**
     * city : {"aqi":"30","co":"0","no2":"10","o3":"94","pm10":"10","pm25":"7","qlty":"优","so2":"3"}
     */

    public CityBean city;

    public CityBean getCity() { return city;}

    public void setCity(CityBean city) { this.city = city;}

    public static class CityBean {
        /**
         * aqi : 30  //空气质量指数
         * co : 0
         * no2 : 10
         * o3 : 94
         * pm10: "10"   //PM10 1小时平均值(ug/m³)
         * pm25: "7",  //PM2.5 1小时平均值(ug/m³)
         * qlty : 优   //空气质量类别，共六个级别，分别：优，良，轻度污染，中度污染，重度污染，严重污染
         *
         */

        public String aqi;
        public String pm10;
        public String pm25;
        public String qlty;

        public String getAqi() { return aqi;}

        public void setAqi(String aqi) { this.aqi = aqi;}

        public String getPm10() { return pm10;}

        public void setPm10(String pm10) { this.pm10 = pm10;}

        public String getPm25() { return pm25;}

        public void setPm25(String pm25) { this.pm25 = pm25;}

        public String getQlty() { return qlty;}

        public void setQlty(String qlty) { this.qlty = qlty;}
    }
}
