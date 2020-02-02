package com.pooai.blesdk.observer;

import com.pooai.blesdk.PooaiToiletCommandManager;
import com.pooai.blesdk.data.ToiletRegisterData;
import com.pooai.blesdk.data.ToiletState;
import com.pooai.blesdk.util.CRCStuff;

import java.util.Observable;

/**
 * 作者：created by xieying on 2020-01-12 18:38
 * 功能：
 */
public class ControlParamsObservable extends Observable {
    private static final String TAG = ControlParamsObservable.class.getSimpleName();

    private static final int LENGTH = 30;

    private static final int START_ADDRESS = 50;

    private static class SingletonHolder {
        private static final ControlParamsObservable INSTANCE = new ControlParamsObservable();
    }

    public static ControlParamsObservable getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void setValue(byte[] values) {
        ToiletState toiletState = PooaiToiletCommandManager.getInstance().getToiletState();
        if (toiletState != ToiletState.CONTROL) {
            return;
        }
        if ((values[0] == 0x01) && (values[1] == 0x03)) {
            int BytesToSend = 5 + values[2];
            int[] crc = CRCStuff.calculateCRC(values, BytesToSend - 2);
            if (crc[0] == ((int) values[BytesToSend - 2] & 0xff) && (crc[1] == ((int) values[BytesToSend - 1] & 0xff))) {
                for (int i = 0; i < LENGTH; i++) {
                    int dat = ((((int) values[3 + 2 * (i)] & 0xff) << 8) | (values[3 + 2 * (i) + 1] & 0xff));
                    ToiletRegisterData.getInstance().setValue(START_ADDRESS + i, dat);
//                    Log.d(TAG, "address = " + (START_ADDRESS + i) + "   value = " + dat);
                }
            }
        }
//        setChanged();
//        notifyObservers(values);
    }

}
