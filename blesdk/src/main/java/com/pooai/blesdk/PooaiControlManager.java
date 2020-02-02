package com.pooai.blesdk;

import com.pooai.blesdk.data.ToiletCommand;
import com.pooai.blesdk.data.ToiletConfig;
import com.pooai.blesdk.data.ToiletRegisterData;
import com.pooai.blesdk.data.ToiletState;

/**
 * 作者：created by xieying on 2020-01-12 17:43
 * 功能：
 */
public class PooaiControlManager {

    private static class SingletonHolder {
        private static final PooaiControlManager INSTANCE = new PooaiControlManager();
    }

    private ToiletRegisterData mToiletRegisterData;

    private PooaiToiletCommandManager mPooaiToiletCommandManager;

    public static PooaiControlManager getInstance() {
        return PooaiControlManager.SingletonHolder.INSTANCE;
    }

    private PooaiControlManager() {
        mToiletRegisterData = ToiletRegisterData.getInstance();
        mPooaiToiletCommandManager = PooaiToiletCommandManager.getInstance();
    }

    //切换成为控制模式(注意 开始控制操作前需要切换成为控制模式)
    public void switchControlMode() {
        PooaiToiletCommandManager.getInstance().changeToiletState(ToiletState.CONTROL);
    }

    public void openLight() {
        if (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONFIG5, 13) != 1) {
            ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, 13, 1);
            mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        }
    }

    public void closeLight() {
        if (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONFIG5, 13) == 1) {
            ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, 13, 0);
            mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        }
    }

    public void openAutoFlip() {
        if (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONFIG5, 14) != 1) {
            ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, 14, 1);
            mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        }
    }

    public void closeAutoFlip() {
        if (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONFIG5, 14) == 1) {
            ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, 14, 0);
            mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        }
    }


}
