package com.pooai.blesdk.util;

/**
 * 作者：created by xieying on 2020-01-12 18:01
 * 功能：
 */
public class PooaiToiletDataUtil {
    public static final int slaverAddress = 0x01;

    //获取03命令（马桶心跳）

    /**
     * 获取03命令（马桶心跳）
     *
     * @param startReg   需要扫描的起始位
     * @param NumOfPoint 需要扫描的位数
     * @return 03命令
     */
    public static byte[] getF03String(int startReg, int NumOfPoint) {
        byte[] buffer = new byte[8];
        buffer[0] = (byte) slaverAddress;
        buffer[1] = 0x03;
        buffer[2] = (byte) ((byte) (startReg >> 8) & 0xff);
        buffer[3] = (byte) (startReg & 0xff);
        buffer[4] = (byte) ((byte) (NumOfPoint >> 8) & 0xff);
        buffer[5] = (byte) (NumOfPoint & 0xff);
        int[] crc = CRCStuff.calculateCRC(buffer, 6);
        buffer[6] = (byte) crc[0];
        buffer[7] = (byte) crc[1];
        return buffer;
    }

    /**
     * 获取06命令(下发给马桶的控制命令)
     *
     * @param regAddress 需要设置的寄存器位置
     * @param Value      需要设置的值
     * @return
     */
    public static byte[] getF06String(int regAddress, int Value) {
        byte[] buffer = new byte[8];
        buffer[0] = (byte) slaverAddress;
        buffer[1] = 0x06;
        buffer[2] = (byte) ((byte) (regAddress >> 8) & 0xff);
        buffer[3] = (byte) (regAddress & 0xff);
        buffer[4] = (byte) (Value >> 8);
        buffer[5] = (byte) (Value & 0xff);
        int[] crc = CRCStuff.calculateCRC(buffer, 6);
        buffer[6] = (byte) crc[0];
        buffer[7] = (byte) crc[1];
        return buffer;
    }
}
