package com.pooai.blesdk.data;

/**
 * 作者：created by xieying on 2020-01-12 18:10
 * 功能：马桶状态（控制or检测or心电）
 */
public enum ToiletState {

    CONTROL(1),

    DETECTION(2),

    HEART(3);

    private int value;

    ToiletState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
