package com.example.momomo.myapplication.hardware;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.IntConsumer;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.momomo.myapplication.utils.BytesUtil;
import com.example.momomo.myapplication.utils.ResponseWaiter;
import com.example.momomo.myapplication.utils.TimerUtil;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.UUID;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public final class MiBand2 {

    public interface TriFloatConsumer { void accept(float x, float y, float z); }

    private static final int REFRESH_TIMEOUT = 100; // 100ms
    private static final int USR_INTERACTION_TIMEOUT = 20000; // 20s
    private static final String TAG = MiBand2.class.getSimpleName();

    private BleDevice mBleDevice;
    private byte[] mAuthKey;
    private byte[] mRand;
    private BandState mState;

    private Consumer<BandState> mDisconnectHandler;
    private SparseArray<Consumer<byte[]>> mNoticeConsumers;
    private IntConsumer mHeartRateHandler;
    private IntConsumer mStepHandler;
    private IntConsumer mBatteryHandler;
    private Timer mHeartRatePingTimer,mStepTimer,mBatteryTimer;
    private Boolean battery_flag=false;

    public MiBand2(@Nullable String macAddress, @Nullable byte[] key) {
        mBleDevice = macAddress == null
                ? null
                : new BleDevice(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress));
        mAuthKey = key;
        mState = new BandState();
        initAuthNoticeConsumer();
    }

    public void setDisconnectHandler(Consumer<BandState> callback) {
        mDisconnectHandler = callback;
    }

    public BandState getState() {
        return mState;
    }

    public String getMacAddress() {
        return mBleDevice == null ? null : mBleDevice.getMac();
    }

    public byte[] getAuthKey() {
        return mAuthKey;
    }

    @Override
    public String toString() {
        return String.format("MiBand2[address=%s, state=%s]", getMacAddress(), mState.toString());
    }


    /**
     * @param refresh true if it's a new connection without historical key
     * @return connected successfully or failed
     */

    // TODO: should be improved
    // synchronized communication
    // doAfter REFRESH_TIMEOUT: let waitForStateUpdate() get the old state before executing a task
    public boolean connect(boolean refresh) {
        mState.reset();

        TimerUtil.doAfter(REFRESH_TIMEOUT, this::bleConnect);
        waitForStateUpdate();
        if (!mState.isBleConnected()) {
            Log.i(TAG, "pair: bleConnect failed, state=" + mState);
            return false;
        }

        TimerUtil.doAfter(REFRESH_TIMEOUT, this::turnOnAuthNotify);
        waitForStateUpdate();
        if (!mState.isAuthNotifyOn()) {
            Log.i(TAG, "pair: failed to enable authentication notification, state=" + mState);
            return false;
        }

        if (mAuthKey == null || refresh) {
            mAuthKey = BytesUtil.random(16);
            TimerUtil.doAfter(REFRESH_TIMEOUT, this::sendKey);
            waitForStateUpdate();
            if (!mState.isKeyGot()) {
                Log.i(TAG, "pair: failed to receive key, state=" + mState);
                return false;
            }
        }

        TimerUtil.doAfter(REFRESH_TIMEOUT, this::requestRand);
        waitForStateUpdate();
        if (!mState.isRandRequested()) {
            Log.i(TAG, "pair: failed to request rand, state=" + mState);
            return false;
        }

        TimerUtil.doAfter(REFRESH_TIMEOUT, this::sendEncryptedRand);
        waitForStateUpdate();
        if (!mState.isEncrypted()) return false;

        TimerUtil.doAfter(REFRESH_TIMEOUT, this::turnOffAuthNotify);
        waitForStateUpdate();
        return true;
    }

    public void disconnect() {
        // TODO: stop working task
        if (mState.isMeasuringHeartRate()) stopMeasureHeartRate();
        if(mState.isMeasuringBattery()) stopMeasureBattery();
        if(mState.isMeasuringStep()) stopMeasureStep();
        BleManager.getInstance().disconnect(mBleDevice);
    }

    public boolean startMeasureHeartRate(IntConsumer heartRateHandler) {
        mHeartRateHandler = heartRateHandler;

        // TODO: check and stop related operations first
        if (!turnOnHeartRateNotify()) {
            Log.e(TAG, "enableHeartRate: failed to turn on heart rate notification");
            return false;
        }

        if (!enableHeartRateContinuousMonitor()) {
            Log.e(TAG, "enableHeartRate: failed to enable heart rate continuous monitor");
            return false;
        }

//        enableHeartRatePing();
        return true;
    }

    public boolean stopMeasureHeartRate() {
        Log.i(TAG, "stopMeasureHeartRate");
        if (!mState.isBleConnected()) {
            Log.i(TAG, "stopMeasureHeartRate: already disconnected");
            return true;
        }
        disableHeartRatePing();
        return disableHeartRateContinuousMonitor() && turnOffHeartRateNotify();
    }
    //test
    public boolean startMeasureStep(IntConsumer stepHandler) {
        mStepHandler = stepHandler;

        // TODO: check and stop related operations first
        if (!turnOnStepNotify()) {
            Log.e(TAG, "enableStep: failed to turn on Step notification");
            return false;
        }
        return true;
    }

    public boolean stopMeasureStep() {
        Log.i(TAG, "stopMeasureStep");
        if (!mState.isBleConnected()) {
            Log.i(TAG, "stopMeasureStep: already disconnected");
            return true;
        }
        disableStep();
        return turnOffStepNotify();
    }
    ///battery
    //test
    public boolean startMeasureBattery(IntConsumer batteryHandler) {
        mBatteryHandler=batteryHandler;

        // TODO: check and stop related operations first

        if (!turnOnBatteryNotify()) {
            Log.e(TAG, "enableBattery: failed to turn on Battery notification");
            return false;
        }
        if(!battery_flag) enableBattery();
        return true;
    }

    public boolean stopMeasureBattery() {
        Log.i(TAG, "stopMeasureBattery");
        if (!mState.isBleConnected()) {
            Log.i(TAG, "stopMeasureBattery: already disconnected");
            return true;
        }
        disableBattery();
        return turnOffBatteryNotify();
    }


    private void initAuthNoticeConsumer() {
        mNoticeConsumers = new SparseArray<>();
        mNoticeConsumers.put(Protocol.Response.SEND_KEY_OK, data -> {
            Log.i(TAG, "accept auth notice: key got");
            mState.setKeyGot(true);
        });
        mNoticeConsumers.put(Protocol.Response.SEND_KEY_OOPS, data -> {
            Log.i(TAG, "accept auth notice: the band failed to receive the key");
            mState.setKeyGot(false);
        });
        mNoticeConsumers.put(Protocol.Response.RAND_OK, data -> {
            Log.i(TAG, "accept auth notice: rand received");
            mRand = data;
            mState.setRandRequested(true);
        });
        mNoticeConsumers.put(Protocol.Response.RAND_OOPS, data -> {
            Log.i(TAG, "accept auth notice: failed to receive rand");
            mState.setRandRequested(false);
        });
        mNoticeConsumers.put(Protocol.Response.AUTH_OK, data -> {
            Log.i(TAG, "accept auth notice: encrypted number matched");
            mState.setEncrypted(true);
        });
        mNoticeConsumers.put(Protocol.Response.AUTH_OOPS, data -> {
            Log.i(TAG, "accept auth notice: encrypted number did not match");
            mState.setEncrypted(false);
        });
    }

    private void bleConnect() {
        BleManager.getInstance().connect(getMacAddress(), new BleGattCallback() {
            @Override public void onStartConnect() {}
            @Override public void onConnectFail(BleDevice bleDevice, BleException exception) {
                mState.setBleConnected(false);
            }
            @Override public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                mBleDevice = bleDevice;
                mState.setBleConnected(true);
            }
            @Override public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                mState.setBleConnected(false);
                if (mDisconnectHandler != null) mDisconnectHandler.accept(mState);
            }
        });
    }

    private void turnOnAuthNotify() {
        BleManager.getInstance().notify(mBleDevice, Protocol.Service.AUTH, Protocol.Characteristic.AUTH, new BleNotifyCallback() {
            @Override public void onNotifySuccess() {
                if (!mState.isAuthNotifyOn()) {
                    mState.setAuthNotify(true);
                    Log.i(TAG, "turnOnAuthNotify: succeeded, or turnOffAuthNotify");
                }
            }
            @Override public void onNotifyFailure(BleException exception) { // FIXME: may have bug
                if (mState.isAuthNotifyOn()) mState.setAuthNotify(false);
//                mState.setAuthNotify(false);
                Log.i(TAG, "turnOnAuthNotify: failed");
            }
            @Override public void onCharacteristicChanged(byte[] data) {
                handleAuthNotification(data);
            }
        });
    }

    private void turnOffAuthNotify() {
        boolean success = BleManager.getInstance().stopNotify(mBleDevice, Protocol.Service.AUTH, Protocol.Characteristic.AUTH);
        if (success) {
            mState.setAuthNotify(false);
            Log.i(TAG, "turnOffAuthNotify: succeeded");
        } else {
            mState.setAuthNotify(true);
            Log.i(TAG, "turnOffAuthNotify: failed");
        }
    }

    private void sendKey() {
        BleManager.getInstance().write(mBleDevice, Protocol.Service.AUTH, Protocol.Characteristic.AUTH,
                BytesUtil.combine(Protocol.Command.SEND_KEY, mAuthKey),
                new BleWriteCallback() {
                    @Override public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.i(TAG, "onWriteSuccess: wrote=" + BytesUtil.toHexStr(justWrite));
                    }
                    @Override public void onWriteFailure(BleException exception) {
                        mState.setKeyGot(false);
                    }
                });
    }

    private void requestRand() {
        BleManager.getInstance().write(mBleDevice, Protocol.Service.AUTH, Protocol.Characteristic.AUTH,
                Protocol.Command.RAND_REQUEST,
                new BleWriteCallback() {
                    @Override public void onWriteSuccess(int current, int total, byte[] justWrite) {}
                    @Override public void onWriteFailure(BleException exception) {
                        mState.setRandRequested(false);
                    }
                });
    }

    private void sendEncryptedRand() {
        byte[] encrypted = aesEncrypt(mRand);
        BleManager.getInstance().write(mBleDevice, Protocol.Service.AUTH, Protocol.Characteristic.AUTH,
                BytesUtil.combine(Protocol.Command.SEND_ENCRYPTED, encrypted),
                new BleWriteCallback() {
                    @Override public void onWriteSuccess(int current, int total, byte[] justWrite) {}
                    @Override public void onWriteFailure(BleException exception) {
                        mState.setEncrypted(false);
                    }
                });
    }

    private byte[] aesEncrypt(byte[] message) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(mAuthKey, "AES"));
            return cipher.doFinal(message);
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "aesEncrypt: " + e.getMessage(), e);
            return null;
        }
    }

    private void handleAuthNotification(byte[] notice) {
        Log.i(TAG, "handleAuthNotification: received notification: " + BytesUtil.toHexStr(notice));
        if (notice.length < 3) {
            Log.w(TAG, "handleAuthNotification: data length < 3!");
            return;
        }
        int head = (int)notice[0] << 16 | (int)notice[1] << 8 | (int)notice[2];
        byte[] body = Arrays.copyOfRange(notice, 3, notice.length);
        Consumer<byte[]> noticeConsumer = mNoticeConsumers.get(head);
        if (noticeConsumer != null) noticeConsumer.accept(body);
        else Log.i(TAG, String.format("handleAuthNotification: unknown auth response header: %04x", head));
    }

    private void waitForStateUpdate() {
        try {
            BandState oldState = new BandState(mState);
            long startTime = System.currentTimeMillis();
            for (;;) {
                Thread.sleep(REFRESH_TIMEOUT);
                if (mState.isNewerThan(oldState)) break;
                if (System.currentTimeMillis() - startTime > USR_INTERACTION_TIMEOUT) break;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "waitForStateUpdate: interrupted: " + e.getMessage(), e);
        }
    }

    private boolean turnOnHeartRateNotify() {
        Log.i(TAG, "turning on heart rate notification");
        ResponseWaiter waiter = new ResponseWaiter(REFRESH_TIMEOUT);
        BleManager.getInstance().notify(
                mBleDevice, Protocol.Service.HEART_RATE, Protocol.Characteristic.HEART_RATE_MEASURE,
                new BleNotifyCallback() {
                    @Override public void onNotifySuccess() {
                        if (!mState.isMeasuringHeartRate()) mState.setHeartNotify(true);
                        waiter.ok();
                        Log.i(TAG, "turnOnHeartRateNotify: succeed");
                    }
                    @Override public void onNotifyFailure(BleException exception) {
                        waiter.fail();
                        mState.setHeartNotify(false);
                        Log.i(TAG, "turnOnHearRateNotify: failed");
                    }
                    @Override public void onCharacteristicChanged(byte[] data) {
                        mState.setHeartMeasuring(true);
                        parseHeartRate(data);
                    }
                }
        );
        return waiter.work();
    }
    //test
    private boolean turnOnStepNotify() {
        Log.i(TAG, "turning on step notification");
        ResponseWaiter waiter = new ResponseWaiter(REFRESH_TIMEOUT);
        BleManager.getInstance().notify(
                mBleDevice, Protocol.Service.BASIC, Protocol.Characteristic.STEPS,
                new BleNotifyCallback() {
                    @Override public void onNotifySuccess() {
                        if (!mState.isMeasuringStep()) mState.setStepNotify(true);
                        waiter.ok();
                        Log.i(TAG, "turnOnStepNotify: succeed");
                    }
                    @Override public void onNotifyFailure(BleException exception) {
                        waiter.fail();
                        mState.setStepNotify(false);
                        Log.i(TAG, "turnOnStepNotify: failed");
                    }
                    @Override public void onCharacteristicChanged(byte[] data) {
                        mState.setStepMeasuring(true);
                        Log.i("STEPSTEP", "步数："+data);
                        parseStep(data);
                    }
                }
        );
        return waiter.work();
    }
//step

    //test
    private boolean turnOnBatteryNotify() {
        Log.i(TAG, "turning on Battery notification");
        ResponseWaiter waiter = new ResponseWaiter(REFRESH_TIMEOUT);
        BleManager.getInstance().notify(
                            mBleDevice, Protocol.Service.BASIC, Protocol.Characteristic.BATTERY,
                new BleNotifyCallback() {
                        @Override public void onNotifySuccess() {
//                            if (!mState.isMeasuringBattery())
                            mState.setBatteryNotify(true);
                            waiter.ok();
                            Log.i(TAG, "turnOnBatteryNotify: succeed");
                        }
                        @Override public void onNotifyFailure(BleException exception) {
                        waiter.fail();
                        mState.setBatteryNotify(false);
                        Log.i(TAG, "turnOnBatteryNotify: failed");
                    }
                    @Override public void onCharacteristicChanged(byte[] data) {
                        mState.setBatteryMeasuring(true);
                        Log.i(TAG, "电量："+data);
                        parseBattery(data);
                    }
                }
        );
        return waiter.work();
    }
//battery

    private boolean turnOffHeartRateNotify() {
        Log.i(TAG, "turning off heart rate notification");
        boolean success = BleManager.getInstance().stopNotify(mBleDevice,
                Protocol.Service.HEART_RATE, Protocol.Characteristic.HEART_RATE_MEASURE);
        if (success) mState.setHeartNotify(false);
        return success;
    }
    //offff
    private boolean turnOffStepNotify() {
        Log.i(TAG, "turning off step notification");
        boolean success = BleManager.getInstance().stopNotify(mBleDevice,
                Protocol.Service.BASIC, Protocol.Characteristic.STEPS);
        if (success) mState.setStepNotify(false);
        return success;
    }
    //pppppp
    //offff
    private boolean turnOffBatteryNotify() {
        Log.i(TAG, "turning off Battery notification");
        boolean success = BleManager.getInstance().stopNotify(mBleDevice,
                Protocol.Service.BASIC, Protocol.Characteristic.BATTERY);
        if (success) mState.setBatteryNotify(false);
        return success;
    }
    //pppppp

    private void parseHeartRate(byte[] data) {
        // In most cases, only data[1] contributes, not sure about data[0], which is usually 0
        int heartRate = (int)data[0] * 0x100 + data[1];
        Log.i(TAG, "parseHeartRate: heartRate=" + heartRate);
        if (mHeartRateHandler != null) mHeartRateHandler.accept(heartRate);
    }
    //test
    private void parseStep(byte[] data) {
        // In most cases, only data[1] contributes, not sure about data[0], which is usually 0
        int step = data[1];
        Log.i(TAG, "parseStep: step=" + step);
        if (mStepHandler != null) mStepHandler.accept(step);
    }
    private void parseBattery(byte[] data) {
        // In most cases, only data[1] contributes, not sure about data[0], which is usually 0
        int info = data[1];
        Log.i(TAG, "parseBattery: Battery=" + info);
        if (mBatteryHandler != null)mBatteryHandler.accept(info);
    }

    private boolean enableHeartRateContinuousMonitor() {
        Log.i(TAG, "enabling heart rate continuous monitor");
        ResponseWaiter waiter = new ResponseWaiter(REFRESH_TIMEOUT);
        BleManager.getInstance().write(
                mBleDevice,
                Protocol.Service.HEART_RATE, Protocol.Characteristic.HEART_RATE_CONTROL,
                Protocol.Command.HEART_START_CONTINUOUS,
                new BleWriteCallback() {
                    @Override public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        waiter.ok();
                        Log.i(TAG, "enableHeartRateContinuousMonitor: succeed");
                    }
                    @Override public void onWriteFailure(BleException exception) {
                        waiter.fail();
                        Log.i(TAG, "enableHeartRateContinuousMonitor: failed");
                    }
                });
        return waiter.work();
    }

    private boolean disableHeartRateContinuousMonitor() {
        Log.i(TAG, "disabling heart rate continuous monitor");
        ResponseWaiter waiter = new ResponseWaiter(REFRESH_TIMEOUT);
        BleManager.getInstance().write(
                mBleDevice,
                Protocol.Service.HEART_RATE, Protocol.Characteristic.HEART_RATE_CONTROL,
                Protocol.Command.HEART_STOP_CONTINUOUS,
                new BleWriteCallback() {
                    @Override public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        mState.setHeartMeasuring(false);
                        waiter.ok();
                        Log.i(TAG, "disableHeartRateContinuousMonitor: succeed");
                    }
                    @Override public void onWriteFailure(BleException exception) {
                        waiter.fail();
                        Log.i(TAG, "disableHeartRateContinuousMonitor: failed");
                    }
                });
        return waiter.work();
    }

    private void enableHeartRatePing() {
        mHeartRatePingTimer = TimerUtil.repeatPer(Protocol.Time.HEART_KEEP_ALIVE_PERIOD, () -> {
            Log.i(TAG, "pinging heart rate monitor...");
            BleManager.getInstance().write(
                    mBleDevice,
                    Protocol.Service.HEART_RATE, Protocol.Characteristic.HEART_RATE_CONTROL,
                    Protocol.Command.HEART_KEEP_ALIVE,
                    new BleWriteCallback() {
                        @Override public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.i(TAG, "pingHeartRate :)");
                            mState.setHeartMeasuring(true);
                        }
                        @Override public void onWriteFailure(BleException exception) {
                            Log.i(TAG, "pingHeartRate :(");
                            mState.setHeartMeasuring(false);
                        }
                    });
        });
    }

private void enableBattery() {
           BleManager.getInstance().read(mBleDevice, Protocol.Service.BASIC, Protocol.Characteristic.BATTERY, new BleReadCallback() {
               @Override
               public void onReadSuccess(byte[] data) {
                   Log.i(TAG, "电量读取成功："+data);
                   parseBattery(data);
                   battery_flag=true;

               }
               @Override
               public void onReadFailure(BleException exception) {
                   Log.i(TAG, "电量读取失败");
               }
           });
    }

    private void disableHeartRatePing() {
        if (mHeartRatePingTimer != null) {
            mHeartRatePingTimer.cancel();
            mHeartRatePingTimer = null;
            mState.setHeartMeasuring(false);
        }
    }
    private void disableStep() {
        if (mStepTimer != null) {
            mStepTimer.cancel();
            mStepTimer = null;
            mState.setStepMeasuring(false);
        }
    }
    private void disableBattery() {
        if (mBatteryTimer != null) {
            mBatteryTimer.cancel();
            mBatteryTimer = null;
            mState.setBatteryMeasuring(false);
        }
    }

}
