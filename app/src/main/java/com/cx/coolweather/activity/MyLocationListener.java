package com.cx.coolweather.activity;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.LBS
 *  @文件名:   MyLocationListener
 *  @创建者:   YHDN
 *  @创建时间:  2017-07-17 14:12
 *  @描述：    TODO
 */

import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class MyLocationListener
        implements BDLocationListener
{
    private TextView positionTv;

    public MyLocationListener(TextView positionTv) {
        this.positionTv = positionTv;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        StringBuilder currentPosition = new StringBuilder();
        currentPosition.append("纬度:")
                       .append(bdLocation.getLatitude())
                       .append("\n");
        currentPosition.append("经度:")
                       .append(bdLocation.getLongitude())
                       .append("\n");
        currentPosition.append("定位方式:");
        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
            currentPosition.append("GPS");
        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            currentPosition.append("网络");
        }
        if (positionTv!=null){
            positionTv.setText(currentPosition);
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
