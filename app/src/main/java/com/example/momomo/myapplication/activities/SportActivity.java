package com.example.momomo.myapplication.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.chart_activity.heart_chart;
import com.example.momomo.myapplication.chart_activity.step_chart;
import com.example.momomo.myapplication.config.Constants;
import com.example.momomo.myapplication.databinding.ActivitySportBinding;
import com.example.momomo.myapplication.hardware.BandState;
import com.example.momomo.myapplication.services.CommService;
import com.example.momomo.myapplication.utils.BytesUtil;
import com.github.jorgecastilloprz.FABProgressCircle;



public final class SportActivity extends AppCompatActivity {

    private static final String TAG = SportActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int
            REQUEST_SCAN_BAND = 2;

    private String mMacAddress;
    private byte[] mAuthKey;

    private ActivitySportBinding mBinding;
    private Messenger mMessenger;
    private FABProgressCircle mFabCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sport);

        mBinding.setConnectTransaction(mConnectTransaction);
        mBinding.setHeartRateTransaction(mHeartRateTransaction);
        mBinding.setStepTransaction(mStepTransaction);
        mBinding.setBatteryTransaction(mBatteryTransaction);

        mFabCircle = mBinding.fabProgressCircle;

        mMessenger = new Messenger(this);
        mMessenger.addHandler(mStateUpdateRequest);
        mMessenger.addHandler(mConnectTransaction);
        mMessenger.addHandler(mHeartRateTransaction);
        mMessenger.addHandler(mStepTransaction);
        mMessenger.addHandler(mBatteryTransaction);

        initBle();
        mStateUpdateRequest.request();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessenger.unregister();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ans = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem pairMenuItem = menu.findItem(R.id.start_pair);
        MenuItem exportMenuItem1 = menu.findItem(R.id.export_data_heart);
        MenuItem exportMenuItem2 = menu.findItem(R.id.export_data_step);

        pairMenuItem.setOnMenuItemClickListener(menuItem -> {
            requestScan();
            return true;
        });
        exportMenuItem1.setIntent(new Intent(this, heart_chart.class));
        exportMenuItem2.setIntent(new Intent(this, step_chart.class));
        return ans;
    }

    private void initBle() {
        BleManager bleManager = BleManager.getInstance();
        bleManager.enableLog(true).setConnectOverTime(10000).setOperateTimeout(5000).init(getApplication());
        if (!bleManager.isSupportBle()) {
            Toast.makeText(this, R.string.toast_not_support_ble, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "initBle: device not support BLE");
            finish();
        }
        new Handler().postDelayed(this::checkAndEnableBt, 500);
    }

    /**
     * @return true if bluetooth is already enabled, otherwise false
     */
    private boolean checkAndEnableBt() {
        if (BleManager.getInstance().isBlueEnable()) {
            return true;
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
            return false;
        }
    }

    private void requestScan() {
        if (!checkAndEnableBt()) return;
        Intent i = new Intent(SportActivity.this, ScanBandActivity.class);
        startActivityForResult(i, REQUEST_SCAN_BAND);
    }

    private void requestPair(String macAddress) {
        mFabCircle.show();
        CommService.startActionPair(this, macAddress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(mBinding.getRoot(), R.string.toast_bt_enabled, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mBinding.getRoot(), R.string.toast_have_to_enable_bt, Snackbar.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_SCAN_BAND:
                if (resultCode == RESULT_OK && data != null) {
                    String macAddress = data.getStringExtra(Constants.Extra.MAC);
                    requestPair(macAddress);
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Request mStateUpdateRequest = new Request() {
        @Override public String[] getActions() {
            return new String[] { Constants.Action.STATE_UPDATE };
        }

        @Override public void request() {
            Intent i = new Intent(SportActivity.this, CommService.class)
                    .setAction(Constants.Action.STATE_UPDATE);
            startService(i);
        }

        @Override public void handleResponse(Intent response) {
            super.handleResponse(response);
            mMacAddress = response.getStringExtra(Constants.Extra.MAC);
            mAuthKey = response.getByteArrayExtra(Constants.Extra.KEY);
            BandState state = new BandState(response.getIntExtra(Constants.Extra.STATE, BandState.DEFAULT_VALUE));
            Log.i(TAG, String.format("StateUpdateRequest.handleResponse: mac=%s, authKey=%s, state=%s",
                    mMacAddress, BytesUtil.toHexStr(mAuthKey), state));
            mBinding.setConnected(state.isEncrypted());
            mHeartRateTransaction.running.set(state.isMeasuringHeartRate());
            mStepTransaction.running.set(state.isMeasuringStep());
            mBatteryTransaction.running.set(state.isMeasuringBattery());
        }
    };

    private Transaction mConnectTransaction = new Transaction() {
        private boolean busy = false;

        @Override public String[] getActions() {
            return new String[] {
                    Constants.Action.PAIR,
                    Constants.Action.CONNECT,
                    Constants.Action.DISCONNECT
            };
        }

        @Override public void start() {
            if (busy) return;
            if (!checkAndEnableBt()) {
                Log.w(TAG, "ConnectTransaction.start: bluetooth disabled");
                return;
            }
            if (mMacAddress == null) requestScan();
            else {
                mFabCircle.show();
                busy = true;
                if (mAuthKey == null) CommService.startActionPair(SportActivity.this, mMacAddress);
                else CommService.startActionConnect(SportActivity.this);
            }
            Log.i(TAG, "ConnectTransaction.start: mac=" + mMacAddress + ", authKey=" + BytesUtil.toHexStr(mAuthKey));
        }

        @Override public void stop() {
            if (busy) return;
            mFabCircle.show();
            busy = true;
            CommService.startActionDisconnect(SportActivity.this, true);
            Log.i(TAG, "ConnectTransaction.stop: disconnect");
        }

        @Override public void handleResponse(Intent response) {
            super.handleResponse(response);
            int status = response.getIntExtra(Constants.Extra.STATUS, Constants.Status.UNKNOWN);
            switch (response.getAction()) {
                case Constants.Action.PAIR:
                case Constants.Action.CONNECT:
                    if (status == Constants.Status.OK) {
                        mFabCircle.beginFinalAnimation();
                        mFabCircle.attachListener(() -> {
                            mBinding.setConnected(true);
                            busy = false;
                            Snackbar.make(mBinding.getRoot(), R.string.toast_connect_ok, Snackbar.LENGTH_SHORT).show();
                        });
                    } else {
                        busy = false;
                        mFabCircle.hide();
                        Snackbar.make(mBinding.getRoot(), R.string.toast_connect_fail, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.Action.DISCONNECT:
                    if (status == Constants.Status.OK) {
                        mFabCircle.beginFinalAnimation();
                        mFabCircle.attachListener(() -> {
                            busy = false;
                            mBinding.setConnected(false);
                            Snackbar.make(mBinding.getRoot(), R.string.toast_disconnect_ok, Snackbar.LENGTH_SHORT).show();
                        });
                    } else {
                        busy = false;
                        mFabCircle.hide();
                        Snackbar.make(mBinding.getRoot(), R.string.toast_disconnect_fail, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    private Transaction mHeartRateTransaction = new Transaction() {
        @Override public String[] getActions() {
            return new String[] {
                    Constants.Action.START_HEART_RATE,
                    Constants.Action.STOP_HEART_RATE,
                    Constants.Action.BROADCAST_HEART_RATE
            };
        }

        @Override public void start() {
            Log.i(TAG, "HeartRateTransaction starting");
            CommService.startActionStartHeartRateMeasure(SportActivity.this);
        }

        @Override public void stop() {
            Log.i(TAG, "HeartRateTransaction stopping");
            CommService.startActionStopHeartRateMeasure(SportActivity.this);
        }

        @Override public void handleResponse(Intent response) {
            super.handleResponse(response);
            int status;
            switch (response.getAction()) {
                case Constants.Action.BROADCAST_HEART_RATE:
                    int heartRate = response.getIntExtra(Constants.Extra.HEART_RATE, -1);
                    mBinding.setHeartRate(heartRate);
                    Log.i(TAG, "HeartRateTransaction.handleResponse: got heart rate = " + heartRate);
                    break;
                case Constants.Action.START_HEART_RATE:
                    status = response.getIntExtra(Constants.Extra.STATUS, Constants.Status.UNKNOWN);
                    Log.i(TAG, "HeartRateTransaction.handleResponse: START_HEART_RATE status=" + status);
                    boolean success = status == Constants.Status.OK;
                    running.set(success);
                    if (!success)
                        Snackbar.make(mBinding.getRoot(), R.string.toast_heart_rate_on_failed, Snackbar.LENGTH_SHORT).show();
                    break;
                case Constants.Action.STOP_HEART_RATE:
                    status = response.getIntExtra(Constants.Extra.STATUS, Constants.Status.UNKNOWN);
                    Log.i(TAG, "HeartRateTransaction.handleResponse: START_HEART_RATE status=" + status);
                    if (status == Constants.Status.OK)
                        running.set(false);
                    else
                        Snackbar.make(mBinding.getRoot(), R.string.toast_heart_rate_off_failed, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //step;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    private Transaction mStepTransaction = new Transaction() {
        @Override public String[] getActions() {
            return new String[] {
                    Constants.Action.START_STEP,
                    Constants.Action.STOP_STEP,
                    Constants.Action.BROADCAST_STEP
            };
        }

        @Override public void start() {
            Log.i(TAG, "StepRateTransaction starting");
            CommService.startActionStartStepMeasure(SportActivity.this);
        }

        @Override public void stop() {
            Log.i(TAG, "StepRateTransaction stopping");
            CommService.startActionStopStepMeasure(SportActivity.this);
        }

        @Override public void handleResponse(Intent response) {
            super.handleResponse(response);
            int status;
            switch (response.getAction()) {
                case Constants.Action.BROADCAST_STEP:
                    int step = response.getIntExtra(Constants.Extra.STEP, -1);
                    mBinding.setStep(step);
                    Log.i(TAG, "StepTransaction.handleResponse: got step = " + step);
                    break;
                case Constants.Action.START_STEP:
                    status = response.getIntExtra(Constants.Extra.STATUS, Constants.Status.UNKNOWN);
                    Log.i(TAG, "StepTransaction.handleResponse: START_STEP status=" + status);
                    boolean success = status == Constants.Status.OK;
                    running.set(success);
                    if (!success)
                        Snackbar.make(mBinding.getRoot(), R.string.toast_step_on_failed, Snackbar.LENGTH_SHORT).show();
                    break;
                case Constants.Action.STOP_STEP:
                    status = response.getIntExtra(Constants.Extra.STATUS, Constants.Status.UNKNOWN);
                    Log.i(TAG, "StepTransaction.handleResponse: START_STEP status=" + status);
                    if (status == Constants.Status.OK)
                        running.set(false);
                    else
                        Snackbar.make(mBinding.getRoot(), R.string.toast_step_off_failed, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //battery;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    private Transaction mBatteryTransaction = new Transaction() {
        @Override public String[] getActions() {
            return new String[] {
                    Constants.Action.START_BATTERY,
                    Constants.Action.STOP_BATTERY,
                    Constants.Action.BROADCAST_BATTERY
            };
        }

        @Override public void start() {
            Log.i(TAG, "BatteryTransaction starting");
            CommService.startActionStartBatteryMeasure(SportActivity.this);
        }

        @Override public void stop() {
            Log.i(TAG, "BatteryTransaction stopping");
            CommService.startActionStopBatteryMeasure(SportActivity.this);
        }

        @Override public void handleResponse(Intent response) {
            super.handleResponse(response);
            int status;
            switch (response.getAction()) {
                case Constants.Action.BROADCAST_BATTERY:
                    int info = response.getIntExtra(Constants.Extra.BATTERY, -1);
                    mBinding.setBattery(info);
                    Log.i(TAG, "BatteryTransaction.handleResponse: got battery = " + info);
                    break;
                case Constants.Action.START_BATTERY:
                    status = response.getIntExtra(Constants.Extra.STATUS, Constants.Status.UNKNOWN);
                    Log.i(TAG, "BatteryTransaction.handleResponse: START_BATTERY status=" + status);
                    boolean success = status == Constants.Status.OK;
                    running.set(success);
                    if (!success)
                        Snackbar.make(mBinding.getRoot(), R.string.toast_battery_on_failed, Snackbar.LENGTH_SHORT).show();
                    break;
                case Constants.Action.STOP_BATTERY:
                    status = response.getIntExtra(Constants.Extra.STATUS, Constants.Status.UNKNOWN);
                    Log.i(TAG, "BatteryTransaction.handleResponse: START_BATTERY status=" + status);
                    if (status == Constants.Status.OK)
                        running.set(false);
                    else
                        Snackbar.make(mBinding.getRoot(), R.string.toast_battery_off_failed, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public static abstract class Request extends Messenger.MessageHandler {
        public abstract void request();
    }


    public static abstract class Transaction extends Messenger.MessageHandler {
        public final ObservableBoolean running = new ObservableBoolean();
        public abstract void start();
        public abstract void stop();
    }
}