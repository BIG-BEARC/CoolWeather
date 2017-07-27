package com.cx.coolweather.lbs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.cx.coolweather.R;
import com.cx.coolweather.app.MyApplication;
import com.cx.coolweather.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

public class LBSActivity
        extends AppCompatActivity
{

    private LocationClient mLocationClient;
    private TextView       mPositionTv;
    private MapView        mMapView;
    private boolean isFirstLocate = true;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mCurrentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(MyApplication.getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        //初始化操作一定要在色图ContentView 之前，否则会报错
        SDKInitializer.initialize(MyApplication.getContext());
        SDKInitializer.setCoordType(CoordType.GCJ02);//默认为BD09LL坐标
        setContentView(R.layout.activity_lbs);
        mPositionTv = (TextView) findViewById(R.id.position_tv);
        mMapView = (MapView) findViewById(R.id.bmapView);
        //获取baidu 地图实例，相当于总控制器
        mBaiduMap = mMapView.getMap();
        //开启位置显示功能
        mBaiduMap.setMyLocationEnabled(true);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LBSActivity.this,
                                              Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LBSActivity.this,
                                              Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(LBSActivity.this,
                                              Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LBSActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {

        initLocation();
        mLocationClient.start();

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {//移动到当前位置只需要程序第一次定位时调用一次即可
            //LatLng类主要存放经纬度，参数一：纬度值，参数二：经度值
            LatLng          ll     = new LatLng(location.getLatitude(), location.getLongitude());
         /*   //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(ll)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);*/
            //获取 MapStatusUpdate 对象
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            //移动到我的位置
            mBaiduMap.animateMapStatus(update);
            //设置地图缩放级别
            update = MapStatusUpdateFactory.zoomTo(16f);
            //完成缩放功能
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);
        //移动到我的位置，并显示出来
/*        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFirstLocate=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    public class MyLocationListener
            implements BDLocationListener
    {


        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            final StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度:")
                           .append(bdLocation.getLatitude())
                           .append("\n");
            currentPosition.append("经度:")
                           .append(bdLocation.getLongitude())
                           .append("\n");
            currentPosition.append("国家:")
                           .append(bdLocation.getCountry())
                           .append("\n");
            currentPosition.append("省:")
                           .append(bdLocation.getProvince())
                           .append("\n");
            currentPosition.append("市:")
                           .append(bdLocation.getCity())
                           .append("\n");
            currentPosition.append("区:")
                           .append(bdLocation.getDistrict())
                           .append("\n");
            currentPosition.append("区:")
                           .append(bdLocation.getStreet())
                           .append("\n");
            currentPosition.append("定位方式:");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);

            }
            //

            //            ToastHelper.show(MainActivity.this, currentPosition.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPositionTv.setText(currentPosition);
                }
            });
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            ToastHelper.show(LBSActivity.this, "必须同意所有权限才能使用地图定位功能");
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    ToastHelper.show(LBSActivity.this, "发生未知错误");
                }
                break;
            default:
                break;
        }
    }
}
