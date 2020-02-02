package com.pooai.blesdk.data;

/**
 * 作者：created by xieying on 2020-01-12 22:46
 * 功能：
 */
public class ToiletCommand {

    private int commandAddress;

    private int commandValue;

    public ToiletCommand(int commandAddress, int commandValue) {
        this.commandAddress = commandAddress;
        this.commandValue = commandValue;
    }

    public int getCommandAddress() {
        return commandAddress;
    }

    public void setCommandAddress(int commandAddress) {
        this.commandAddress = commandAddress;
    }

    public int getCommandValue() {
        return commandValue;
    }

    public void setCommandValue(int commandValue) {
        this.commandValue = commandValue;
    }
}
