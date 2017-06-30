package com.cx.coolweather.gson;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.gson
 *  @文件名:   Suggestion
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/29 15:45
 *  @描述：   生活指数，仅限国内城市
 */

import com.google.gson.annotations.SerializedName;

public class Suggestion {

    /**
     * brf 舒适度
     * comf : {"brf":"较不舒适","txt":"白天天气多云，同时会感到有些热，不很舒适。"}//舒适度指数
     * cw : {"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}//洗车指数
     * drsg : {"brf":"炎热","txt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}//穿衣指数
     * flu : {"brf":"少发","txt":"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"}//感冒指数
     * sport : {"brf":"较适宜","txt":"天气较好，户外运动请注意防晒。推荐您进行室内运动。"} //运动指数
     * trav : {"brf":"较适宜","txt":"天气较好，温度较高，天气较热，但有微风相伴，还是比较适宜旅游的，不过外出时要注意防暑防晒哦！"}//旅游指数
     * uv : {"brf":"中等","txt":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}//紫外线指数

     */
    @SerializedName("comf")
    public ComfortBean  comfort;//舒适度指数
    @SerializedName("cw")
    public CarWashBean    carWash;//洗车指数
    public DrsgBean  drsg;//穿衣指数
    public FluBean   flu;//感冒指数
    public SportBean sport;//运动指数
    public TravBean  trav;//旅游指数
    public UvBean    uv;//紫外线指数


    public ComfortBean getComfort() { return comfort;}

    public void setComfort(ComfortBean comfort) { this.comfort = comfort;}

    public CarWashBean getCarWash() { return carWash;}

    public void setCarWash(CarWashBean carWash) { this.carWash = carWash;}

    public DrsgBean getDrsg() { return drsg;}

    public void setDrsg(DrsgBean drsg) { this.drsg = drsg;}

    public FluBean getFlu() { return flu;}

    public void setFlu(FluBean flu) { this.flu = flu;}

    public SportBean getSport() { return sport;}

    public void setSport(SportBean sport) { this.sport = sport;}

    public TravBean getTrav() { return trav;}

    public void setTrav(TravBean trav) { this.trav = trav;}

    public UvBean getUv() { return uv;}

    public void setUv(UvBean uv) { this.uv = uv;}

    public static class ComfortBean {
        /**
         * brf : 较不舒适
         * txt : 白天天气多云，同时会感到有些热，不很舒适。
         */

        private String brf;
        @SerializedName("txt")
        public String info;

        public String getBrf() { return brf;}

        public void setBrf(String brf) { this.brf = brf;}

        public String getInfo() { return info;}

        public void setInfo(String info) { this.info = info;}
    }

    public static class CarWashBean {
        /**
         * brf : 较适宜
         * info : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
         */

        private String brf;
        @SerializedName("txt")
        public String info;

        public String getBrf() { return brf;}

        public void setBrf(String brf) { this.brf = brf;}

        public String getInfo() { return info;}

        public void setInfo(String info) { this.info = info;}
    }

    public static class DrsgBean {
        /**
         * brf : 炎热
         * txt : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
         */

        private String brf;
        @SerializedName("txt")
        private String info;

        public String getBrf() { return brf;}

        public void setBrf(String brf) { this.brf = brf;}

        public String getInfo() { return info;}

        public void setInfo(String info) { this.info = info;}
    }

    public static class FluBean {
        /**
         * brf : 少发
         * txt : 各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。
         */

        private String brf;
        @SerializedName("txt")
        private String info;

        public String getBrf() { return brf;}

        public void setBrf(String brf) { this.brf = brf;}

        public String getInfo() { return info;}

        public void setInfo(String info) { this.info = info;}
    }

    public static class SportBean {
        /**
         * brf : 较适宜
         * txt : 天气较好，户外运动请注意防晒。推荐您进行室内运动。
         */

        private String brf;
        @SerializedName("txt")
        public String info;

        public String getBrf() { return brf;}

        public void setBrf(String brf) { this.brf = brf;}

        public String getInfo() { return info;}

        public void setInfo(String info) { this.info = info;}
    }

    public static class TravBean {
        /**
         * brf : 较适宜
         * txt : 天气较好，温度较高，天气较热，但有微风相伴，还是比较适宜旅游的，不过外出时要注意防暑防晒哦！
         */

        private String brf;
        @SerializedName("txt")
        public String info;

        public String getBrf() { return brf;}

        public void setBrf(String brf) { this.brf = brf;}

        public String getInfo() { return info;}

        public void setInfo(String info) { this.info = info;}
    }

    public static class UvBean {
        /**
         * brf : 中等
         * txt : 属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。
         */

        private String brf;
        @SerializedName("txt")
        public String info;

        public String getBrf() { return brf;}

        public void setBrf(String brf) { this.brf = brf;}

        public String getInfo() { return info;}

        public void setInfo(String info) { this.info = info;}
    }
}
