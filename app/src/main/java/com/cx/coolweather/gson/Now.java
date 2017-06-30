package com.cx.coolweather.gson;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.gson
 *  @文件名:   Now
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/29 15:40
 *  @描述：   实况天气
 */

import com.google.gson.annotations.SerializedName;

public class Now {

    /**
     * cond : {//天气状况
     * code: "100",  //天气状况代码
     * txt: "晴" //天气状况描述
     * hum : 20% //相对湿度（%）
     * tmp : 32 //温度
     * vis : 10 //能见度（km）
     * wind : {//风力风向
     * "dir":"北风",//风向
     * "sc":"3级",//风力
     * "spd":"15"}//风速（kmph）
     */
    @SerializedName("cond")
    public MoreBean more;
    public String   hum;
    @SerializedName("tmp")
    public String   temperature;
    public String   vis;
    public WindBean wind;

    public MoreBean getCond() { return more;}

    public void setCond(MoreBean more) { this.more = more;}

    public String getHum() { return hum;}

    public void setHum(String hum) { this.hum = hum;}

    public String getTemperature() { return temperature;}

    public void setTemperature(String temperature) { this.temperature = temperature;}

    public String getVis() { return vis;}

    public void setVis(String vis) { this.vis = vis;}

    public WindBean getWind() { return wind;}

    public void setWind(WindBean wind) { this.wind = wind;}

    public static class MoreBean {
        /**
         * code : 100
         * txt : 晴
         */

        private String code;
        @SerializedName("txt")
        public String info;

        public String getCode() { return code;}

        public void setCode(String code) { this.code = code;}

        public String getInfo() { return info;}

        public void setInfo(String info) { this.info = info;}
    }

    public static class WindBean {
        /**
         * dir : 北风
         * sc : 3级
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
