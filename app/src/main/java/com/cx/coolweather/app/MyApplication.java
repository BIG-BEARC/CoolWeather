package com.cx.coolweather.app;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.app
 *  @文件名:   MyApplication
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/28 9:44
 *  @描述：    TODO
 */

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePalApplication.initialize(context);
        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
//        CrashHandler.getInstance().init(context);
    }
    public static Context getContext(){
        return context;
    }
}
