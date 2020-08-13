package com.example.ble;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.BleLog;
import cn.com.heaton.blelibrary.ble.callback.BleConnectCallback;
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback;
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback;
import cn.com.heaton.blelibrary.ble.model.BleDevice;
import cn.com.heaton.blelibrary.ble.utils.UuidUtils;

/**
 * Describe:
 * Created by xieying on 2020/8/13
 */
public class BleManager {
    //UUID
    private static final String UUID_SERVER = "0000ffe0-0000-1000-8000-00805f9b34fb";

    private static final String UUID_NOTIFY = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private Ble<BleDevice> mBle;

    private BleDevice mConnectBleDevice;

    private List<OnBleScanListener> mScanListener = new ArrayList<>();

    private List<OnBleConnectListener> mConnectListener = new ArrayList<>();

    private BleScanCallback<BleDevice> mScanCallBack = new BleScanCallback<BleDevice>() {
        @Override
        public void onLeScan(BleDevice device, int rssi, byte[] scanRecord) {
            sendScanningListener(device);
        }

        @Override
        public void onStart() {
            super.onStart();
            sendScanStartListener();
        }

        @Override
        public void onStop() {
            super.onStop();
            sendScanStopListener();
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            sendScanFailListener();
        }
    };

    private BleConnectCallback<BleDevice> mConnectCallBack = new BleConnectCallback<BleDevice>() {
        @Override
        public void onConnectionChanged(BleDevice device) {
            if (device.isConnected()) {
                mConnectBleDevice = device;
                sendConnectedListener(device);
                //已连接
            } else if (device.isConnecting()) {
                sendConnectingListener(device);
                //连接中
            } else if (device.isDisconnected()) {
                //断开连接
                mConnectBleDevice = null;
                sendDisconnectedListener(device);
            }
        }

        @Override
        public void onConnectTimeOut(BleDevice device) {
            super.onConnectTimeOut(device);
            //连接超时
            sendConnectErrorListener();
        }

        @Override
        public void onConnectCancel(BleDevice device) {
            super.onConnectCancel(device);
            //连接取消
            sendConnectErrorListener();
        }

        @Override
        public void onServicesDiscovered(BleDevice device, BluetoothGatt gatt) {
            super.onServicesDiscovered(device, gatt);

        }

        @Override
        public void onReady(BleDevice device) {
            super.onReady(device);
        }

        @Override
        public void onConnectException(BleDevice device, int errorCode) {
            super.onConnectException(device, errorCode);
            sendConnectErrorListener();
        }
    };

    private static class SingletonHolder {
        private static BleManager INSTANCE = new BleManager();
    }

    public static BleManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 初始化，建议放在Application中进行初始化
     * @param application
     */
    public void init(Application application) {
        mBle = Ble.options()//开启配置
                .setLogBleEnable(true)//设置是否输出打印蓝牙日志（非正式打包请设置为true，以便于调试）
                .setThrowBleException(true)//设置是否抛出蓝牙异常 （默认true）
                .setAutoConnect(false)//设置是否自动连接 （默认false）
                .setIgnoreRepeat(false)//设置是否过滤扫描到的设备(已扫描到的不会再次扫描)
                .setConnectTimeout(10 * 1000)//设置连接超时时长（默认10*1000 ms）
                .setMaxConnectNum(7)//最大连接数量
                .setScanPeriod(12 * 1000)//设置扫描时长（默认10*1000 ms）
                .setUuidService(UUID.fromString(UuidUtils.uuid16To128("fd00")))//设置主服务的uuid（必填）
                .setUuidWriteCha(UUID.fromString(UuidUtils.uuid16To128("fd01")))//设置可写特征的uuid （必填,否则写入失败）
                .create(application, new Ble.InitCallback() {
                    @Override
                    public void success() {
                        BleLog.e("MainApplication", "初始化成功");
                    }

                    @Override
                    public void failed(int failedCode) {
                        BleLog.e("MainApplication", "初始化失败：" + failedCode);
                    }
                });
    }

    /**
     * 开始扫描
     */
    public void startScan() {
        checkBle();
        if (!mBle.isScanning()) {
            mBle.startScan(mScanCallBack, 10000);
        }
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        checkBle();
        mBle.stopScan();
    }

    /**
     * 连接设备
     * @param bleDevice bleDevice
     */
    public void connectDevice(BleDevice bleDevice) {
        checkBle();
        mConnectBleDevice = null;
        mBle.connect(bleDevice, mConnectCallBack);
    }

    /**
     * 设备断开连接
     */
    private void disConnectDevice(){
        if (mConnectBleDevice != null && mConnectBleDevice.isConnected()) {
            mBle.disconnect(mConnectBleDevice);
        }
    }

    /**
     * 写入数据
     * @param bytes 数据
     */
    private void writeData(byte[] bytes) {
        checkBle();
        if (mConnectBleDevice != null) {
            mBle.writeByUuid(mConnectBleDevice
                    , bytes, UUID.fromString(UUID_SERVER), UUID.fromString(UUID_NOTIFY), new BleWriteCallback<BleDevice>() {
                        @Override
                        public void onWriteSuccess(BleDevice device, BluetoothGattCharacteristic characteristic) {

                        }

                        @Override
                        public void onWriteFailed(BleDevice device, int failedCode) {
                            super.onWriteFailed(device, failedCode);
                        }
                    });
        }
    }

    private void sendScanStartListener() {
        if (mScanListener != null) {
            for (OnBleScanListener listener : mScanListener) {
                listener.scanStart();
            }
        }
    }

    private void sendScanningListener(BleDevice bleDevice) {
        if (mScanListener != null) {
            for (OnBleScanListener listener : mScanListener) {
                listener.scanning(bleDevice);
            }
        }
    }

    private void sendScanStopListener() {
        if (mScanListener != null) {
            for (OnBleScanListener listener : mScanListener) {
                listener.scanStop();
            }
        }
    }

    private void sendScanFailListener() {
        if (mScanListener != null) {
            for (OnBleScanListener listener : mScanListener) {
                listener.scanFail();
            }
        }
    }

    private void sendConnectErrorListener() {
        if (mConnectListener != null) {
            for (OnBleConnectListener listener : mConnectListener) {
                listener.connectError();
            }
        }
    }

    private void sendConnectedListener(BleDevice bleDevice) {
        if (mConnectListener != null) {
            for (OnBleConnectListener listener : mConnectListener) {
                listener.connected(bleDevice);
            }
        }
    }

    private void sendConnectingListener(BleDevice bleDevice){
        if (mConnectListener != null) {
            for (OnBleConnectListener listener : mConnectListener) {
                listener.connecting(bleDevice);
            }
        }
    }

    private void sendDisconnectedListener(BleDevice bleDevice) {
        if (mConnectListener != null) {
            for (OnBleConnectListener listener : mConnectListener) {
                listener.disConnected(bleDevice);
            }
        }
    }


    public void addBleScanListener(OnBleScanListener onBleScanListener) {
        if (onBleScanListener != null && !mScanListener.contains(onBleScanListener)) {
            mScanListener.add(onBleScanListener);
        }
    }

    public void removeBleScanListener(OnBleScanListener onBleScanListener) {
        if (mScanListener != null) {
            mScanListener.remove(onBleScanListener);
        }
    }

    public void addBleConnectListener(OnBleConnectListener onBleConnectListener) {
        if (mConnectListener != null && !mConnectListener.contains(onBleConnectListener)) {
            mConnectListener.add(onBleConnectListener);
        }
    }

    public void removeBleConnectListener(OnBleConnectListener onBleConnectListener) {
        if (mConnectListener != null) {
            mConnectListener.remove(onBleConnectListener);
        }
    }

    private void checkBle() {
        if (mBle == null) {
            throw new IllegalStateException("BleManager should be initialization");
        }
    }

    public void release() {
        mBle.cancelCallback(mScanCallBack);
        mBle.cancelCallback(mConnectCallBack);
        mBle.released();
    }


}
