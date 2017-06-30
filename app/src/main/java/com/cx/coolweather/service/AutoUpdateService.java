package com.cx.coolweather.service;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.service
 *  @文件名:   AutoUpdateService
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/30 18:29
 *  @描述：    TODO
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cx.coolweather.gson.Weather;
import com.cx.coolweather.util.HttpUtil;
import com.cx.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService
        extends Service
{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        updateWeather();
        updateBingPic();
        AlarmManager  manager       = (AlarmManager) getSystemService(ALARM_SERVICE);
        int           anHour        = 8 * 60 * 60 * 1000;//8小时毫秒数
        long          triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent        serviceIntent = new Intent(this, AutoUpdateService.class);
        PendingIntent pi            = PendingIntent.getService(this, 0, serviceIntent, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        Log.e("AutoUpdateService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException
            {
                final String bingPic = response.body()
                                               .string();
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(
                        AutoUpdateService.this)
                                                                 .edit();
                edit.putString("bing_pic", bingPic);
                edit.apply();

            }
        });
    }

    /**
     * 更新天气
     */
    private void updateWeather() {
        SharedPreferences prefs         = PreferenceManager.getDefaultSharedPreferences(this);
        String            weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather   = Utility.handleWeatherResponse(weatherString);
            String  weatherId = weather.basic.weatherId;
            String  weathrUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=2a942c9b885e495f93557f8169ca7a82";
            HttpUtil.sendOkHttpRequest(weathrUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException
                {
                    String responseText = response.body()
                                                  .string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(
                                AutoUpdateService.this)
                                                                         .edit();
                        edit.putString("weather", responseText);
                        edit.apply();
                    }

                }
            });
        }
    }
}
