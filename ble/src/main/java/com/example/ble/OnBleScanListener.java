package com.example.ble;

import cn.com.heaton.blelibrary.ble.model.BleDevice;

/**
 * Describe:
 * Created by xieying on 2020/8/13
 */
public interface OnBleScanListener {

    void scanStart();

    void scanning(BleDevice bleDevice);

    void scanStop();

    void scanFail();
}
