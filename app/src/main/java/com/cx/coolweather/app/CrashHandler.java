package com.cx.coolweather.app;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.app
 *  @文件名:   CrashHandler
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/28 14:17
 *  @描述：    异常捕获器，UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.cx.coolweather.util.ToastHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cx.coolweather.BuildConfig.DEBUG;

public class CrashHandler
        implements Thread.UncaughtExceptionHandler
{
    public static final String TAG = "CrashHandler";
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 静态内部类单例模式
     */
    private CrashHandler() {}


    private static class InstanceHolder {
        private static final CrashHandler instance = new CrashHandler();
    }

    public static CrashHandler getInstance() {
        return InstanceHolder.instance;
    }

    // 获取SDcard路径
    public static       String SDCARD        = android.os.Environment.getExternalStorageDirectory()
                                                                     .getAbsolutePath();
    // 错误日志路径
    public static final String lOG_PATH      = SDCARD + "/yihua/caze2/log/";
    // 下载路径
    public static final String DOWNLOAD_PATH = SDCARD + "/yihua/caze2/download/";

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     *
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     *
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

     /*   try {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
            uploadExceptionToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {

            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ToastHelper.show(mContext, "平台开小差了.");
                Looper.loop();
            }
        }.start();

        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集手机设备参数信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        try {
            //应用的版本名称和版本号
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null
                                     ? "null"
                                     : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                //android版本号
                infos.put("OS Version:", Build.VERSION.RELEASE+"_"+Build.VERSION.SDK_INT);
                //手机制造商
                infos.put("Vendor", Build.MANUFACTURER);
                //手机型号
                infos.put("Model", Build.MODEL);
                //cpu架构
                infos.put("CPU ABI:", Build.CPU_ABI);
                //程序 crash 时间
                infos.put("crashTime", formatter.format(new Date()));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(),
                          field.get(null)
                               .toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    public String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key   = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer      writer      = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        //打印出当前调用栈信息
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();

        //打印错误日志到控制台。方便开发时能看到错误信息。
        //        LogHelper.e("Case2Handler", result);

        sb.append(result);
        try {
            long   timestamp = System.currentTimeMillis();
            String time      = formatter.format(new Date());
            String fileName  = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState()
                           .equals(Environment.MEDIA_MOUNTED))
            {
                String path = "/sdcard/yihua/caze2/log/";
                File   dir  = new File(lOG_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString()
                            .getBytes());
                fos.close();
            }else { //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
                if (DEBUG) {
                    Log.w(TAG, "sdcard unmounted,skip dump exception");

                }
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }
}
