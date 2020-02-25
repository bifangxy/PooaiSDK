package com.pooai.blesdk;

import androidx.annotation.IntRange;

import com.pooai.blesdk.data.ToiletCommand;
import com.pooai.blesdk.data.ToiletConfig;
import com.pooai.blesdk.data.ToiletRegisterData;
import com.pooai.blesdk.data.ToiletState;

/**
 * 作者：created by xieying on 2020-01-12 17:43
 * 功能：马桶控制管理类
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

    /**
     * 开始臀洗
     */
    public void startHipWash() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 9, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 停止臀洗
     */
    public void stopHipWash() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 0, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取臀洗状态
     * @return
     */
    public boolean isHipWash() {
        return mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONTROL, 9) == 1;
    }

    /**
     * 开始妇洗
     */
    public void startWomanWash() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 10, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取妇洗状态
     * @return
     */
    public boolean isWomanWash() {
        return mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONTROL, 10) == 1;
    }


    /**
     * 停止妇洗
     */
    public void stopWomanWash() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 0, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 开始通便
     */
    public void startLaxative() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 8, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 停止通便
     */
    public void stopLaxative() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 0, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取通便状态
     * @return
     */
    public boolean isLaxative() {
        return mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONTROL, 8) == 1;
    }

    /**
     * 开始烘干
     */
    public void startDrying() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 3, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);

    }

    /**
     * 停止烘干
     */
    public void stopDrying() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 0, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取烘干状态
     * @return
     */
    public boolean isDrying() {
        return mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONTROL, 3) == 1;
    }

    /**
     * 开始按摩
     */
    public void startMassage() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 15, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 停止按摩
     */
    public void stopMassage() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 15, 0);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取按摩状态
     * @return
     */
    public boolean isMassage() {
        return mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONTROL, 15) == 1;
    }

    /**
     * 开始雾化
     */
    public void startAtomize() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 2, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 停止雾化
     */
    public void stopAtomize() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONTROL, 0, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取雾化状态
     * @return
     */
    public boolean isAtomize() {
        return mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONTROL, 2) == 1;
    }

    /**
     * 调节坐垫温度
     *
     * @param gear 档位
     */
    public void cushionTemp(@IntRange(from = 0, to = 5) int gear) {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegister4HighCommandbefore(ToiletConfig.REGISTER_TOILET_CONFIG1, gear);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取坐垫温度档位
     * @return
     */
    public int getCushionTempStall() {
        return mToiletRegisterData.getRegister4HighValuebefore(ToiletConfig.REGISTER_TOILET_CONFIG1);
    }

    /**
     * 调节水温
     *
     * @param gear 档位
     */
    public void waterTemp(@IntRange(from = 0, to = 5) int gear) {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegister4HighCommand(ToiletConfig.REGISTER_TOILET_CONFIG1, gear);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }


    /**
     * 获取水温档位
     * @return
     */
    public int getWaterTempStall() {
        return mToiletRegisterData.getRegister4HighValue(ToiletConfig.REGISTER_TOILET_CONFIG1);
    }

    /**
     * 调节喷嘴位置
     *
     * @param gear 档位
     */
    public void nozzlePosition(@IntRange(from = 0, to = 5) int gear) {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegister4LowCommandbefore(ToiletConfig.REGISTER_TOILET_CONFIG1, gear);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取喷嘴位置档位
     * @return
     */
    public int getNozzlePositionStall() {
        return mToiletRegisterData.getRegister4LowValuebefore( ToiletConfig.REGISTER_TOILET_CONFIG1);
    }

    /**
     * 调节风温
     *
     * @param gear 档位
     */
    public void windTemp(@IntRange(from = 0, to = 5) int gear) {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegister4LowCommand(ToiletConfig.REGISTER_TOILET_CONFIG1, gear);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取风温档位
     * @return
     */
    public int getWindTempStall() {
        return mToiletRegisterData.getRegister4LowValue(ToiletConfig.REGISTER_TOILET_CONFIG1);
    }

    /**
     * 调节水压
     *
     * @param gear 档位
     */
    public void waterPressure(@IntRange(from = 0, to = 5) int gear) {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegister4HighCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, gear);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
    }

    /**
     * 获取水压档位
     * @return
     */
    public int getWaterPressureStall() {
        return mToiletRegisterData.getRegister4HighValue(ToiletConfig.REGISTER_TOILET_CONFIG2);
    }

    /**
     * 开双盖
     */
    public void openLid() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterCommand(115, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        ToiletCommand toiletCommand1 = mToiletRegisterData.getRegister4LowCommandbefore(ToiletConfig.REGISTER_TOILET_CONTROL2, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand1);
    }

    /**
     * 开单盖
     */
    public void openHalfLid() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterCommand(115, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        ToiletCommand toiletCommand1 = mToiletRegisterData.getRegister4LowCommandbefore(ToiletConfig.REGISTER_TOILET_CONTROL2, 3);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand1);
    }

    /**
     * 关盖
     */
    public void closeLid() {
        ToiletCommand toiletCommand = mToiletRegisterData.getRegisterCommand(115, 1);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        ToiletCommand toiletCommand1 = mToiletRegisterData.getRegister4LowCommandbefore(ToiletConfig.REGISTER_TOILET_CONTROL2, 2);
        mPooaiToiletCommandManager.addToiletCommand(toiletCommand1);
    }

    /**
     * 开夜灯
     */
    public void openLight() {
        if (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONFIG5, 13) != 1) {
            ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, 13, 1);
            mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        }
    }

    /**
     * 关夜灯
     */
    public void closeLight() {
        if (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONFIG5, 13) == 1) {
            ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, 13, 0);
            mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        }
    }

    /**
     * 打开自动翻盖
     */
    public void openAutoFlip() {
        if (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONFIG5, 14) != 1) {
            ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, 14, 1);
            mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        }
    }

    /**
     * 关闭自动翻盖
     */
    public void closeAutoFlip() {
        if (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_CONFIG5, 14) == 1) {
            ToiletCommand toiletCommand = mToiletRegisterData.getRegisterBitCommand(ToiletConfig.REGISTER_TOILET_CONFIG5, 14, 0);
            mPooaiToiletCommandManager.addToiletCommand(toiletCommand);
        }
    }

    /**
     * 是否已着坐
     *
     * @return
     */
    public boolean isSeat() {
        return mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_STATUS1, 13) == 1;
    }

    /**
     * 水温调节是否异常
     *
     * @return
     */
    public boolean isWaterTempError() {
        return (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_ERROR, 0) == 1
                || mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_ERROR, 1) == 1
                || mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_ERROR, 2) == 1
                || mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_ERROR, 5) == 1
                || mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_ERROR, 9) == 1);
    }

    /**
     * 风温调节是否异常
     *
     * @return
     */
    public boolean isDryingTempError() {
        return (mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_ERROR, 4) == 1
                || mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_ERROR, 10) == 1);
    }

    /**
     * 坐温调节是否异常
     *
     * @return
     */
    public boolean isCushionTempError() {
        return mToiletRegisterData.getRegisterBitValue(ToiletConfig.REGISTER_TOILET_ERROR, 3) == 1;
    }


}
