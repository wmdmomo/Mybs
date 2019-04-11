package com.example.momomo.myapplication.map_activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;

import androidx.annotation.Nullable;


public class TraceActivity extends Activity {
     private Context mContext = null;
     private LBSTraceClient mTraceClient=null;
     private Trace mTrace = null;
     private setOnTraceListener mTraceListener = null;
    // 轨迹服务ID
     private long serviceId = 211089;
    // 设备标识
     private String entityName =null;
     // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
     private  boolean isNeedObjectStorage =false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        entityName = getImei(mContext);
        // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
        isNeedObjectStorage = false;
        init();
        mTraceClient.startTrace(mTrace, mTraceListener);
        mTraceClient.startGather(mTraceListener);
        mTraceClient.stopTrace(mTrace, mTraceListener);
    }

    private void init(){
        // 初始化轨迹服务
        mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(mContext);
        mTraceListener = new setOnTraceListener();
        // 定位周期(单位:秒)
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
    }

    public static String getImei(Context context) {
        String imei;
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(0);
            }
        else{
            imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        } catch (SecurityException e) {
            imei = "myTrace";
        }
        return imei;
    }

    public class setOnTraceListener implements OnTraceListener{
        @Override
        public void onBindServiceCallback(int i, String s) {}
        // 开启服务回调
        @Override
        public void onStartTraceCallback(int status, String message) {}
        // 停止服务回调
        @Override
        public void onStopTraceCallback(int status, String message) {}
        // 开启采集回调
        @Override
        public void onStartGatherCallback(int status, String message) {}
        // 停止采集回调
        @Override
        public void onStopGatherCallback(int status, String message) {}

        @Override
        public void onPushCallback(byte b, PushMessage pushMessage) {}

        @Override
        public void onInitBOSCallback(int i, String s) {}
    }
}
