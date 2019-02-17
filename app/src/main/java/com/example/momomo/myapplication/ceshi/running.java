package com.example.momomo.myapplication.ceshi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.example.momomo.myapplication.Adapter.deviceshowAdapter;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.hardware.MiBand2;

import java.util.ArrayList;
import java.util.List;

public class running extends AppCompatActivity {
    private Button scan;
    private static final int SCAN_TIMEOUT = 10 * 1000;
    private List<BleDevice> devicesitemList=new ArrayList<>();
    private deviceshowAdapter deviceshowAdapter;
    private MiBand2 miBand2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        scan=(Button) findViewById(R.id.scan);
        BleInit();
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.device_rv);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(running.this,1);
        deviceshowAdapter = new deviceshowAdapter(running.this, devicesitemList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(deviceshowAdapter);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BleManager.getInstance().isBlueEnable()){
                    scanDevice();
                }else{
                    Toast.makeText(running.this,"蓝牙未打开",Toast.LENGTH_SHORT).show();
                    checkAndEnableBt();
                }
            }
        });
        deviceshowAdapter.setOnDeviceClickListener(new deviceshowAdapter.OnDeviceClickListener() {
            @Override
            public void onClick(int position) {
                final BleDevice bleDevice=devicesitemList.get(position);
                connect(bleDevice);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager bleManager = BleManager.getInstance();
        if (bleManager.getScanSate() == BleScanState.STATE_SCANNING)
            bleManager.cancelScan();
    }
    private void BleInit(){
        BleManager bleManager=BleManager.getInstance();
        bleManager.enableLog(true).setConnectOverTime(10000).setOperateTimeout(5000).init(getApplication());
        if(!bleManager.isSupportBle()){
            Toast.makeText(this,"该设备不支持蓝牙",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            checkAndEnableBt();
        }
    }
    private void checkAndEnableBt() {
        if (BleManager.getInstance().isBlueEnable()) {
            return;
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
    }
    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice.getMac(), new BleGattCallback() {
            @Override
            public void onStartConnect() {
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
//                Toast.makeText(MainActivity.this, getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
//                progressDialog.dismiss();
                Log.i("TAG", "连接成功");
//                miBand2=new MiBand2(bleDevice.getMac(),);

                // 搜索指定 Characteristic UUID，并 Write
//                searchSpecifiedCharacteristicUuid(bleDevice, WRITE_CHARACTERISTIC_UUID);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
//                progressDialog.dismiss();
//
//                mBluetoothQuickAdapter.remove(mBluetoothQuickAdapter.getData().indexOf(bleDevice));

//                Toast.makeText(MainActivity.this, getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void scanDevice() {
        BleManager bleManager=BleManager.getInstance();
        bleManager.initScanRule(new BleScanRuleConfig.Builder()
                .setScanTimeOut(SCAN_TIMEOUT)
                .build());
        bleManager.scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

            }

            @Override
            public void onScanStarted(boolean success) {
                deviceshowAdapter.clear();
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                String devicemac=bleDevice.getMac();
                String devicename=bleDevice.getName();
                Log.i("TAG", String.format("onScanning: find device[name=%s, mac=%s]", devicename, devicemac));
                deviceshowAdapter.adddevice(bleDevice);
            }
        });

    }

}
