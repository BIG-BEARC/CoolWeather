package com.cx.coolweather.util;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.util
 *  @文件名:   ToastHelper
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/28 14:45
 *  @描述：    自定义Toast 优化
 */

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    private static Toast sToast;

    public static void show(Context context, String text) {
        if (sToast == null) {
            sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(text);
        }
        sToast.show();
    }

    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }
}
