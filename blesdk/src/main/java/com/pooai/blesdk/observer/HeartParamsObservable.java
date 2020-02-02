package com.pooai.blesdk.observer;

import com.pooai.blesdk.PooaiToiletCommandManager;
import com.pooai.blesdk.data.ToiletState;

import java.util.Observable;

/**
 * 作者：created by xieying on 2020-01-28 16:05
 * 功能：
 */
public class HeartParamsObservable extends Observable {

    private OnHeartReceiverListener mOnHeartReceiverListener;

    private static class SingletonHolder {
        private static final HeartParamsObservable INSTANCE = new HeartParamsObservable();
    }

    public static HeartParamsObservable getInstance() {
        return HeartParamsObservable.SingletonHolder.INSTANCE;
    }

    public void setValue(byte[] values) {
        if (PooaiToiletCommandManager.getInstance().getToiletState() != ToiletState.HEART) {
            return;
        }
        if (mOnHeartReceiverListener != null) {
            mOnHeartReceiverListener.receive(values);
        }
//        setChanged();
//        notifyObservers(values);
    }

    public void setOnHeartReceiverListener(OnHeartReceiverListener onHeartReceiverListener) {
        mOnHeartReceiverListener = onHeartReceiverListener;
    }

    public interface OnHeartReceiverListener {
        void receive(byte[] data);
    }
}
