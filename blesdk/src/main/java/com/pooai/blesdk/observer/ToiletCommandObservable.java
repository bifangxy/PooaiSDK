package com.pooai.blesdk.observer;

import com.pooai.blesdk.data.ToiletState;

import java.util.Observable;

/**
 * 作者：created by xieying on 2020-01-26 13:53
 * 功能：
 */
public class ToiletCommandObservable extends Observable {

    private ToiletState mToiletState = ToiletState.CONTROL;

    private boolean mIsContinue;

    private int mReadSize = 0;

    private int mRealSize = 0;

    private byte[] mDataList;


    private static class SingletonHolder {
        private static final ToiletCommandObservable INSTANCE = new ToiletCommandObservable();
    }

    public static ToiletCommandObservable getInstance() {
        return ToiletCommandObservable.SingletonHolder.INSTANCE;
    }

    public void setToiletState(ToiletState toiletState) {
        mToiletState = toiletState;
    }

    public void setValue(byte[] values) {
        switch (mToiletState) {
            case CONTROL:
            case DETECTION:
                conversionData(values);
                break;
            case HEART:
                conversionHeartData(values);
                break;
        }
    }

    //一条完整的命令一次发不过来，需要根据协议拼接完整
    private void conversionData(byte[] data) {
        if (!mIsContinue) {
            if (data[0] == 1 && data[1] == 3) {
                mReadSize = data[2] + 5;
                mDataList = new byte[mReadSize];
                mRealSize = data.length;
                if (mDataList.length > data.length) {
                    System.arraycopy(data, 0, mDataList, 0, data.length);
                    mIsContinue = true;
                }
            }
        } else {
            try {
                System.arraycopy(data, 0, mDataList, mRealSize, data.length);
                mRealSize = mRealSize + data.length;
                if (mRealSize >= mReadSize) {
                    mIsContinue = false;
                    if (mToiletState == ToiletState.CONTROL) {
                        ControlParamsObservable.getInstance().setValue(mDataList);
                    } else if (mToiletState == ToiletState.DETECTION) {
                        DetectionParamsObservable.getInstance().setValue(mDataList);
                    }
                }
            } catch (Exception e) {
                mIsContinue = false;
            }

        }
    }

    private void conversionHeartData(byte[] data) {
        if (mRealSize <= 100) {
            if (mDataList.length != 120) {
                mDataList = new byte[120];
            }
            System.arraycopy(data, 0, mDataList, mRealSize, data.length);
            mRealSize = data.length + mRealSize;
        } else {
            HeartParamsObservable.getInstance().setValue(mDataList);
            mDataList = new byte[120];
            System.arraycopy(data, 0, mDataList, 0, data.length);
            mRealSize = data.length;
        }
    }

}
