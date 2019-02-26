package com.example.momomo.myapplication.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.activities.SportActivity;
import com.example.momomo.myapplication.config.Constants;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.batteryData;
import com.example.momomo.myapplication.data_save.heartData;
import com.example.momomo.myapplication.data_save.stepData;
import com.example.momomo.myapplication.hardware.BandState;
import com.example.momomo.myapplication.hardware.MiBand2;
import com.example.momomo.myapplication.utils.BytesUtil;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class CommService extends Service {

    private static final String TAG = CommService.class.getSimpleName();

    private static final int WAKELOCK_TIMEOUT = 36000000; // 10 hours

    private static final String ONGOING_CHANNEL = "ONGOING";
    private static final int HR_MEASURE_NOTIFY = 1;

    private MiBand2 mBand;
    private Thread mHeartRateWorkThread,mStepWorkThread,mBatteryThread;
    private SharedPreferences mSettings;
    private PowerManager.WakeLock mWakeLock;
    private heartData heartData;
    private stepData stepData;
    private batteryData batteryData;
    private String username;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        mSettings = getSharedPreferences(Constants.Settings.DEVICE, Context.MODE_PRIVATE);
        mBand = loadBandFromSettings();
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "myapp:mywakelocktag");
        final saveVarible app = (saveVarible) getApplication();
        int userId = app.getUserId();
        User user = LitePal.find(User.class, userId);
        username=user.getName();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        disconnect();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String action;
        if (intent == null || (action = intent.getAction()) == null) {
            stopSelfResult(startId);
            return START_NOT_STICKY;
        }
        Log.i(TAG, "onStartCommand: action=" + action);
        switch (action) {
            case Constants.Action.STATE_UPDATE:
                broadcastState(mBand.getState());
                break;
            case Constants.Action.PAIR:
                pairAndConnect(intent.getStringExtra(Constants.Extra.MAC));
                break;
            case Constants.Action.CONNECT:
                connect();
                break;
            case Constants.Action.DISCONNECT:
                disconnect(intent);
                break;
            case Constants.Action.START_HEART_RATE:
                startHeartRateMeasure();
                break;
            case Constants.Action.STOP_HEART_RATE:
                stopHeartRateMeasure();
                break;
            case Constants.Action.START_STEP:
                startStepMeasure();
                break;
            case Constants.Action.STOP_STEP:
                stopStepMeasure();
                break;
            case Constants.Action.START_BATTERY:
                startBatteryMeasure();
                break;
            case Constants.Action.STOP_BATTERY:
                stopBatteryMeasure();
                break;

            default:
                Log.w(TAG, "onStartCommand: Unknown action");
        }
        return START_NOT_STICKY;
    }

    private void broadcastState(BandState state) {
        // TODO:
        // 1.sync -> async
        // 2. add more information, e.g. battery
        String mac;
        byte[] authKey;
        int stateValue;
        if (mBand == null) {
            mac = null;
            authKey = null;
            stateValue = 0;
        } else {
            mac = mBand.getMacAddress();
            authKey = mBand.getAuthKey();
            stateValue = state.getValue();
        }
        Intent i = new Intent(Constants.Action.STATE_UPDATE)
                .putExtra(Constants.Extra.MAC, mac)
                .putExtra(Constants.Extra.KEY, authKey)
                .putExtra(Constants.Extra.STATE, stateValue);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        Log.i(TAG, "broadcastState: macAddress=" + mac + ", authKey=" + BytesUtil.toHexStr(authKey) + ", state=" + state);
    }

    private void pairAndConnect(String macAddress) {
        if (mHeartRateWorkThread != null && mHeartRateWorkThread.isAlive()) {
            Log.i(TAG, "pairAndConnect: the last thread is still working, current task canceled");
            return;
        }
        disconnect();
        mBand = createMiBandInstanceWithCallback(macAddress, null);
        mHeartRateWorkThread = new Thread(() -> {
            boolean success = mBand.connect(true);
            Intent response = new Intent(Constants.Action.PAIR);
            if (success) {
                Log.i(TAG, "pairAndConnect: paired successfully, state=" + mBand.getState());
                mSettings.edit()
                        .putString(Constants.Settings.DEVICE_MAC, mBand.getMacAddress())
                        .putString(Constants.Settings.DEVICE_KEY, BytesUtil.toHexStr(mBand.getAuthKey()))
                        .apply();
                response.putExtra(Constants.Extra.STATUS, Constants.Status.OK);
            } else {
                Log.i(TAG, "pairAndConnect: failed to pair, state=" + mBand.getState());
                response.putExtra(Constants.Extra.STATUS, Constants.Status.FAILED);
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            mHeartRateWorkThread = null;
        });
        mHeartRateWorkThread.start();
    }

    private void connect() {
        if (mHeartRateWorkThread != null && mHeartRateWorkThread.isAlive()) {
            Log.w(TAG, "pairAndConnect: the last thread is still working, current task canceled");
            return;
        }
        mHeartRateWorkThread = new Thread(() -> {
            boolean success = mBand.connect(false);
            Intent response = new Intent(Constants.Action.CONNECT);
            if (success) {
                Log.i(TAG, "connect: connected successfully, state=" + mBand.getState());
                response.putExtra(Constants.Extra.STATUS, Constants.Status.OK);
            } else {
                Log.i(TAG, "connect: failed to connect, state=" + mBand.getState());
                response.putExtra(Constants.Extra.STATUS, Constants.Status.FAILED);
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            mHeartRateWorkThread = null;
        });
        mHeartRateWorkThread.start();
    }

    // synchronized
    private void disconnect() {
        if (mBand.getState().isBleConnected()) mBand.disconnect();
    }

    // asynchronized
    private void disconnect(Intent i) {
        new Thread(() -> {
            disconnect();
            if (i.getBooleanExtra(Constants.Extra.WITH_RESPONSE, false)) {
                Intent response = new Intent(Constants.Action.DISCONNECT)
                        .putExtra(Constants.Extra.STATUS, Constants.Status.OK);
                LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            }
        }).start();
    }

    private void startHeartRateMeasure() {
        if (mHeartRateWorkThread != null && mHeartRateWorkThread.isAlive()) {
            Log.w(TAG, "startHeartRateMeasure: the last thread is still working, current task canceled");
            return;
        }
        lockAwake();
        foregroundNotify();

        mHeartRateWorkThread = new Thread(() -> {
            boolean success;
            if (mBand.getState().isMeasuringHeartRate()) {
                success = true;
                Log.i(TAG, "startHeartRateMeasure: already measuring");
            } else {
                success = mBand.startMeasureHeartRate(heartRate -> {
                    heartData=new heartData();
                    heartData.setUser(username);
                    heartData.setTime(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
                    heartData.setHeart(heartRate);
                    heartData.save();
                    Intent i = new Intent(Constants.Action.BROADCAST_HEART_RATE)
                            .putExtra(Constants.Extra.HEART_RATE, heartRate);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                });
            }
            Intent response = new Intent(Constants.Action.START_HEART_RATE)
                    .putExtra(Constants.Extra.STATUS, success ? Constants.Status.OK : Constants.Status.FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            mHeartRateWorkThread = null;
        });
        mHeartRateWorkThread.start();
    }

    private void stopHeartRateMeasure() {
        if (mWakeLock != null && mWakeLock.isHeld()) mWakeLock.release();

        if (mHeartRateWorkThread != null && mHeartRateWorkThread.isAlive()) {
            Log.w(TAG, "startHeartRateMeasure: the last thread is still working, current task canceled");
            return;
        }

        stopForeground(true);

        mHeartRateWorkThread = new Thread(() -> {
            boolean success = mBand.stopMeasureHeartRate();
            Intent response = new Intent(Constants.Action.STOP_HEART_RATE)
                    .putExtra(Constants.Extra.STATUS, success ? Constants.Status.OK : Constants.Status.FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            mHeartRateWorkThread = null;
        });
        mHeartRateWorkThread.start();
    }
    //test step
    // TODO: to be refactored
    private void startStepMeasure() {
        if (mStepWorkThread != null && mStepWorkThread.isAlive()) {
            Log.w(TAG, "startStepMeasure: the last thread is still working, current task canceled");
            return;
        }

        lockAwake();
        foregroundNotify();

        mStepWorkThread = new Thread(() -> {
            boolean success;
            if (mBand.getState().isMeasuringStep()) {
                success = true;
                Log.i(TAG, "startStepMeasure: already measuring");
            } else {
                success = mBand.startMeasureStep(step -> {
                    stepData=new stepData();
                    stepData.setUser(username);
                    stepData.setStep(step);
                    stepData.setTime(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
                    stepData.save();

                    Intent i = new Intent(Constants.Action.BROADCAST_STEP)
                            .putExtra(Constants.Extra.STEP, step);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                });
            }
            Intent response = new Intent(Constants.Action.START_STEP)
                    .putExtra(Constants.Extra.STATUS, success ? Constants.Status.OK : Constants.Status.FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            mStepWorkThread = null;
        });
        mStepWorkThread.start();
    }

    private void stopStepMeasure() {
        if (mWakeLock != null && mWakeLock.isHeld()) mWakeLock.release();

        Log.i(TAG, "stopStepMeasure: stopping step measurement");

        // TODO: release lock
        if (mStepWorkThread != null && mStepWorkThread.isAlive()) {
            Log.w(TAG, "stopStepMeasure: the last thread is still working, current task canceled");
            return;
        }

        stopForeground(true);

        mStepWorkThread = new Thread(() -> {
            boolean success = mBand.stopMeasureStep();
            Intent response = new Intent(Constants.Action.STOP_STEP)
                    .putExtra(Constants.Extra.STATUS, success ? Constants.Status.OK : Constants.Status.FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            mStepWorkThread = null;
        });
        mStepWorkThread.start();
    }

    //test baterry
    // TODO: to be refactored
    private void startBatteryMeasure() {
        if (mBatteryThread != null && mBatteryThread.isAlive()) {
            Log.w(TAG, "startBatteryMeasure: the last thread is still working, current task canceled");
            return;
        }

        lockAwake();
        foregroundNotify();

        mBatteryThread = new Thread(() -> {
            boolean success;
            if (mBand.getState().isMeasuringBattery()) {
                success = true;
                Log.i(TAG, "startBatteryMeasure: already measuring");
            } else {
                success = mBand.startMeasureBattery(info -> {
                    batteryData=new batteryData();
                    batteryData.setUser(username);
                    batteryData.setBattery(info);
                    batteryData.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


                    Intent i = new Intent(Constants.Action.BROADCAST_BATTERY)
                            .putExtra(Constants.Extra.BATTERY, info);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                });
            }
            Intent response = new Intent(Constants.Action.START_BATTERY)
                    .putExtra(Constants.Extra.STATUS, success ? Constants.Status.OK : Constants.Status.FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            mBatteryThread = null;
        });
        mBatteryThread.start();
    }

    private void stopBatteryMeasure() {
        if (mWakeLock != null && mWakeLock.isHeld()) mWakeLock.release();

        Log.i(TAG, "stopBatteryMeasure: stopping battery measurement");

        // TODO: release lock
        if (mBatteryThread != null && mBatteryThread.isAlive()) {
            Log.w(TAG, "stopBatteryMeasure: the last thread is still working, current task canceled");
            return;
        }

        stopForeground(true);

        mBatteryThread = new Thread(() -> {
            boolean success = mBand.stopMeasureBattery();
            Intent response = new Intent(Constants.Action.STOP_BATTERY)
                    .putExtra(Constants.Extra.STATUS, success ? Constants.Status.OK : Constants.Status.FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            mBatteryThread = null;
        });
        mBatteryThread.start();
    }

    private MiBand2 loadBandFromSettings() {
        String macAddress = mSettings.getString(Constants.Settings.DEVICE_MAC, null);
        byte[] authKey = BytesUtil.hexStrToBytes(mSettings.getString(Constants.Settings.DEVICE_KEY, null));
        return createMiBandInstanceWithCallback(macAddress, authKey);
    }

    private MiBand2 createMiBandInstanceWithCallback(String macAddress, byte[] authKey) {
        MiBand2 miBand = new MiBand2(macAddress, authKey);
        miBand.setDisconnectHandler(bandState -> {
            Log.i(TAG, "onDisconnect: state=" + bandState);
            broadcastState(mBand.getState());
            // TODO: reconnect for several times
        });
        return miBand;
    }

    private void foregroundNotify() {
        int IMPORTANCE = 3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ONGOING_CHANNEL, "Ongoing", IMPORTANCE);
            NotificationManager notifyMgr = getSystemService(NotificationManager.class);
            if (notifyMgr != null) notifyMgr.createNotificationChannel(channel);
        }
        Intent i = new Intent(this, SportActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new NotificationCompat.Builder(this, ONGOING_CHANNEL)
                .setSmallIcon(R.drawable.ic_heart_red_64dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_heart_red_64dp))
                .setContentTitle(getString(R.string.label_fitness_recorder))
                .setContentText(getString(R.string.notify_measuring))
                .setContentIntent(pendingIntent)
                .setPriority(IMPORTANCE)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .build();
        startForeground(HR_MEASURE_NOTIFY, notification);
    }

    private void lockAwake() {
        PowerManager powerMgr = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerMgr != null) {
//            mWakeLock = powerMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "KeepAlive");
            mWakeLock.acquire(WAKELOCK_TIMEOUT);
        }
    }

    public static void startActionStateUpdate(Context context) {
        Intent i = new Intent(context, CommService.class)
                .setAction(Constants.Action.STATE_UPDATE);
        context.startService(i);
    }

    public static void startActionPair(Context context, String macAddress) {
        Intent i = new Intent(context, CommService.class)
                .setAction(Constants.Action.PAIR)
                .putExtra(Constants.Extra.MAC, macAddress);
        context.startService(i);
    }

    public static void startActionConnect(Context context) {
        Intent i = new Intent(context, CommService.class).setAction(Constants.Action.CONNECT);
        context.startService(i);
    }

    public static void startActionDisconnect(Context context, boolean withResponse) {
        Intent i = new Intent(context, CommService.class)
                .setAction(Constants.Action.DISCONNECT)
                .putExtra(Constants.Extra.WITH_RESPONSE, withResponse);
        context.startService(i);
    }

    public static void startActionStartHeartRateMeasure(Context context) {
        Intent i = new Intent(context, CommService.class).setAction(Constants.Action.START_HEART_RATE);
        context.startService(i);
    }

    public static void startActionStopHeartRateMeasure(Context context) {
        Intent i = new Intent(context, CommService.class).setAction(Constants.Action.STOP_HEART_RATE);
        context.startService(i);
    }
    public static void startActionStartStepMeasure(Context context) {
        Intent i = new Intent(context, CommService.class).setAction(Constants.Action.START_STEP);
        context.startService(i);
    }
    public static void startActionStopStepMeasure(Context context) {
        Intent i = new Intent(context, CommService.class).setAction(Constants.Action.STOP_STEP);
        context.startService(i);
    }
    public static void startActionStartBatteryMeasure(Context context) {
        Intent i = new Intent(context, CommService.class).setAction(Constants.Action.START_BATTERY);
        context.startService(i);
    }
    public static void startActionStopBatteryMeasure(Context context) {
        Intent i = new Intent(context, CommService.class).setAction(Constants.Action.STOP_BATTERY);
        context.startService(i);
    }
}
