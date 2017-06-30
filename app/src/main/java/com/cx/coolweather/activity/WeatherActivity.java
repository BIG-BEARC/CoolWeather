package com.cx.coolweather.activity;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.activity
 *  @文件名:   WeatherActivity
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/30 11:18
 *  @描述：    TODO
 */

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cx.coolweather.R;
import com.cx.coolweather.gson.Forecast;
import com.cx.coolweather.gson.Weather;
import com.cx.coolweather.util.HttpUtil;
import com.cx.coolweather.util.ToastHelper;
import com.cx.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity
        extends AppCompatActivity
{

    private ScrollView         mWeatherLayout;
    private TextView           mTitleCity;
    private TextView           mTitleUpdateTime;
    private TextView           mDegreeText;
    private TextView           mWeatherInfoText;
    private TextView           mAqiText;
    private TextView           mPm25Text;
    private TextView           mComfortText;
    private TextView           mCarWashText;
    private TextView           mSportText;
    private LinearLayout       mForecastLayout;
    private ImageView          mBingPicImg;
    public  SwipeRefreshLayout mSwipeRefresh;
    private String             mWeatherId;
    public  DrawerLayout       mDrawerLayout;
    private Button             mNvaBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarSetting();
        setContentView(R.layout.activity_weather);

        initView();
        initData();
        initListener();
    }


    /**
     * 沉浸状态栏
     */
    private void statusBarSetting() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initView() {
        mWeatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        mTitleCity = (TextView) findViewById(R.id.title_city);
        mTitleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        mDegreeText = (TextView) findViewById(R.id.degreee_text);
        mWeatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        mAqiText = (TextView) findViewById(R.id.aqi_text);
        mPm25Text = (TextView) findViewById(R.id.pm25_text);
        mComfortText = (TextView) findViewById(R.id.comfort_text);
        mCarWashText = (TextView) findViewById(R.id.car_wash_text);
        mSportText = (TextView) findViewById(R.id.sport_text);
        mForecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        mBingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.deawer_layout);
        mNvaBtn = (Button) findViewById(R.id.nav_button);
    }

    private void initData() {
        SharedPreferences prefs         = PreferenceManager.getDefaultSharedPreferences(this);
        String            weatherString = prefs.getString("weather", null);
        String            bingPic       = prefs.getString("bing_pic", null);
        if (weatherString != null) {
            //有缓存时，直接解析缓存数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWrsyhrtInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            mWeatherId = getIntent().getStringExtra("weather_id");
            mWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        if (bingPic != null) {
            Glide.with(this)
                 .load(bingPic)
                 .into(mBingPicImg);
        } else {
            loadBingPic();
        }
    }

    private void initListener() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        mNvaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
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
                        WeatherActivity.this)
                                                                 .edit();
                edit.putString("bing_pic", bingPic);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this)
                             .load(bingPic)
                             .into(mBingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 根据天气 id 请求城市天气信息
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        String weathrUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=2a942c9b885e495f93557f8169ca7a82";
        HttpUtil.sendOkHttpRequest(weathrUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.show(WeatherActivity.this, "获取天气信息失败");
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException
            {
                final String responseText = response.body()
                                                    .string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(
                                    WeatherActivity.this)
                                                                             .edit();
                            edit.putString("weather", responseText);
                            edit.apply();
                            showWrsyhrtInfo(weather);
                        } else {
                            ToastHelper.show(WeatherActivity.this, "获取天气信息失败");
                        }
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 处理并展示 Weather 实体类中的数据
     * @param weather
     */
    private void showWrsyhrtInfo(Weather weather) {
        String cityName    = weather.basic.cityName;
        String updateTime  = weather.basic.update.updateTimeLocal.split(" ")[1];
        String degree      = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        mTitleCity.setText(cityName);
        mTitleUpdateTime.setText(updateTime);
        mDegreeText.setText(degree);
        mWeatherInfoText.setText(weatherInfo);
        mForecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this)
                                      .inflate(R.layout.forecast_item, mForecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText  = (TextView) view.findViewById(R.id.max_text);
            TextView minText  = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.infoDay);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            mForecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            mAqiText.setText(weather.aqi.city.aqi);
            mPm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport   = "运动建议：" + weather.suggestion.sport.info;
        mComfortText.setText(comfort);
        mCarWashText.setText(carWash);
        mSportText.setText(sport);
        mWeatherLayout.setVisibility(View.VISIBLE);
    }


}

