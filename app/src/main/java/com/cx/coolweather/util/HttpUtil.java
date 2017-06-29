package com.cx.coolweather.util;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.httputil
 *  @文件名:   HttpUtil
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/27 18:42
 *  @描述：    TODO
 */

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request      request  = new Request.Builder().url(address)
                                                   .build();
        client.newCall(request).enqueue(callback);
    }

}
