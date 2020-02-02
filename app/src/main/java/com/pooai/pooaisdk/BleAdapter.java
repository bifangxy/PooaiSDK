package com.pooai.pooaisdk;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 作者：created by xieying on 2020-02-01 14:01
 * 功能：
 */
public class BleAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {
    public BleAdapter(@Nullable List<BluetoothDevice> data) {
        super(R.layout.item_ble_device, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BluetoothDevice item) {
        helper.setText(R.id.tv_ble_device_name,item.getName()+":"+item.getAddress());
    }
}
