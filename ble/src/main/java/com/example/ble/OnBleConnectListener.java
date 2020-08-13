package com.example.ble;

import cn.com.heaton.blelibrary.ble.model.BleDevice;

/**
 * Describe:
 * Created by xieying on 2020/8/13
 */
public interface OnBleConnectListener {

    void connectError();

    void connecting(BleDevice bleDevice);

    void connected(BleDevice bleDevice);

    void disConnected(BleDevice bleDevice);
}
