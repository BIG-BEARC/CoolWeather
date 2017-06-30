package com.cx.coolweather.gson;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.gson
 *  @文件名:   Forecast
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/29 16:09
 *  @描述：     天气预报，单天
 */

import com.google.gson.annotations.SerializedName;

public class Forecast {

    /**
     * date : 2015-07-02 //预报日期
     * cond : {"txt_d":"晴",白天
     * "txt_n":"晴"}晚上
     * hum : 14//相对湿度（%）
     * tmp : {"max":"34", //最高温度
     * "min":"18"}//最低温度
     * vis : 10//能见度（km）
     * wind : {"deg":"339","dir":"东南风","sc":"3-4","spd":"15"}
     */

    public String          date;
    @SerializedName("cond")
    public MoreBean        more;
    public String          hum;
    @SerializedName("tmp")
    public TemperatureBean temperature;
    public String          vis;
    public WindBean        wind;

    public String getDate() { return date;}

    public void setDate(String date) { this.date = date;}


    public MoreBean getMore() { return more;}

    public void setMore(MoreBean more) { this.more = more;}

    public String getHum() { return hum;}

    public void setHum(String hum) { this.hum = hum;}


    public TemperatureBean getTemperature() { return temperature;}

    public void setTemperature(TemperatureBean temperature) { this.temperature = temperature;}

    public String getVis() { return vis;}

    public void setVis(String vis) { this.vis = vis;}

    public WindBean getWind() { return wind;}

    public void setWind(WindBean wind) { this.wind = wind;}


    public static class MoreBean {
        /**
         * txt_d : 晴
         * txt_n : 晴
         */
        @SerializedName("txt_d")
        public String infoDay;
        public String infoNight;


        public String getInfoDay() { return infoDay;}

        public void setInfoDay(String infoDay) { this.infoDay = infoDay;}

        public String getInfoNight() { return infoNight;}

        public void setInfoNight(String infoNight) { this.infoNight = infoNight;}
    }

    public static class TemperatureBean {
        /**
         * max : 34
         * min : 18
         */

        public String max;
        public String min;

        public String getMax() { return max;}

        public void setMax(String max) { this.max = max;}

        public String getMin() { return min;}

        public void setMin(String min) { this.min = min;}
    }

    public static class WindBean {
        /**
         * dir : 东南风
         * sc : 3-4
         * spd : 15
         */

        private String dir;
        private String sc;
        private String spd;


        public String getDir() { return dir;}

        public void setDir(String dir) { this.dir = dir;}

        public String getSc() { return sc;}

        public void setSc(String sc) { this.sc = sc;}

        public String getSpd() { return spd;}

        public void setSpd(String spd) { this.spd = spd;}
    }
}
