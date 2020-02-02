package com.pooai.blesdk.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：created by xieying on 2020-01-12 22:52
 * 功能：
 */
public class ToiletRegisterData {

    private static List<RegisterValue> mRegisterValueList;

    private static class SingletonHolder {
        private static final ToiletRegisterData INSTANCE = new ToiletRegisterData();
    }

    public static ToiletRegisterData getInstance() {
        return ToiletRegisterData.SingletonHolder.INSTANCE;
    }

    private ToiletRegisterData() {
        mRegisterValueList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mRegisterValueList.add(new RegisterValue(i, 0));
        }
    }

    /**
     * 设置寄存器的值
     *
     * @param address 地址
     * @param value   值
     */
    public void setValue(int address, int value) {
        for (RegisterValue registerValue : mRegisterValueList) {
            if (address == registerValue.getAddress()) {
                registerValue.setValue(value);
            }
        }
    }

    /**
     * 获取寄存器的值
     *
     * @param address 地址
     * @return
     */
    public int getRegisterValue(int address) {
        for (RegisterValue registerValue : mRegisterValueList) {
            if (registerValue.getAddress() == address) {
                return registerValue.getValue();
            }
        }
        return -1;
    }


    public int getRegisterBitValue(int regAddr, int bit) {
        int v = 1 << bit;
        int reg = getRegisterValue(regAddr);
        if ((reg & v) == v)
            return 1;
        else
            return 0;
    }

    //读取寄存器高8位的值
    public int getRegisterHighValue(int regAddr) {
        int reg = getRegisterValue(regAddr);
        return (reg & 0xff00) >> 8;
    }

    //读取寄存器低8位的值
    public int getRegisterLowValue(int regAddr) {
        int reg = getRegisterValue(regAddr);
        return (reg & 0x00ff);
    }

    //读取寄存器1-3位的值
    public int getRegister4LowValuebefore(int regAddr) {
        int reg = getRegisterValue(regAddr);
        return (reg & 0x000f);
    }

    //读取寄存器4-7位的值
    public int getRegister4LowValue(int regAddr) {
        int reg = getRegisterValue(regAddr);
        return (reg & 0x00f0) >> 4;
    }

    //读取寄存器8-11位的值
    public int getRegister4HighValuebefore(int regAddr) {
        int reg = getRegisterValue(regAddr);
        return (reg & 0x0f00) >> 8;
    }

    //读取寄存器12-15位的值
    public int getRegister4HighValue(int regAddr) {
        int reg = getRegisterValue(regAddr);
        return (reg & 0xf000) >> 12;
    }

    public ToiletCommand getRegisterCommand(int address, int value) {
        return new ToiletCommand(address, value);
    }

    public ToiletCommand getRegisterBitCommand(int regAddr, int bit, int value) {
        int curValue = getRegisterValue(regAddr);
        if (value == 1) {
            curValue |= (int) (1 << bit);
        } else {
            curValue &= ~(1 << bit);
        }
        return new ToiletCommand(regAddr, curValue);
    }

    //获取修改后寄存器高8位的值
    public ToiletCommand getRegisterHighCommand(int regAddr, int value) {
        int curValue = getRegisterValue(regAddr);
        curValue = (curValue & 0x00ff) | ((value << 8) & 0xff00);
        return new ToiletCommand(regAddr, curValue);
    }

    //获取修改后寄存器低8位的值
    public ToiletCommand getRegisterLowCommand(int regAddr, int value) {
        int curValue = getRegisterValue(regAddr);
        curValue = (curValue & 0xff00) | ((value) & 0x00ff);
        return new ToiletCommand(regAddr, curValue);
    }

    //获取修改后寄存器1-3位的值
    public ToiletCommand getRegister4LowCommandbefore(int regAddr, int value) {
        int curValue = getRegisterValue(regAddr);
        curValue = (curValue & 0xfff0) | ((value) & 0x000f);
        return new ToiletCommand(regAddr, curValue);
    }

    //获取修改后寄存器4-7位的值
    public ToiletCommand getRegister4LowCommand(int regAddr, int value) {
        int curValue = getRegisterValue(regAddr);
        curValue = (curValue & 0xff0f) | ((value << 4) & 0x00f0);
        return new ToiletCommand(regAddr, value);
    }

    //获取修改后寄存器8-11位的值
    public ToiletCommand getRegister4HighCommandbefore(int regAddr, int value) {
        int curValue = getRegisterValue(regAddr);
        curValue = (curValue & 0xf0ff) | ((value << 8));
        return new ToiletCommand(regAddr, curValue);
    }

    //获取修改后寄存器12-15位的值
    public ToiletCommand getRegister4HighCommand(int regAddr, int value) {
        int curValue = getRegisterValue(regAddr);
        curValue = (curValue & 0x0fff) | ((value << 12));
        return new ToiletCommand(regAddr, curValue);
    }

}
