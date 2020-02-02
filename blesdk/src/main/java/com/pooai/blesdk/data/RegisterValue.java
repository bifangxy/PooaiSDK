package com.pooai.blesdk.data;

/**
 * 作者：created by xieying on 2020-01-12 22:54
 * 功能：
 */
public class RegisterValue {

    private int address;

    private int value;

    public RegisterValue(int address, int value) {
        this.address = address;
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
