package com.pooai.blesdk;

import android.util.Log;

import androidx.annotation.NonNull;

import com.pooai.blesdk.data.ToiletCommand;
import com.pooai.blesdk.data.ToiletState;
import com.pooai.blesdk.observer.ToiletCommandObservable;
import com.pooai.blesdk.util.PooaiToiletDataUtil;
import com.pooai.blesdk.util.TimerTaskUtil;

import java.util.LinkedList;

import io.reactivex.disposables.Disposable;

/**
 * 作者：created by xieying on 2020-01-12 18:00
 * 功能 马桶命令管理
 */
public class PooaiToiletCommandManager {

    private static final String TAG = PooaiToiletCommandManager.class.getSimpleName();

    private ToiletState mToiletState = ToiletState.CONTROL;

    private static final int SCAN_LENGTH = 30;

    private int startAddress = 50;

    private PooaiBleManager mPooaiBleManager;

    private TimerTaskUtil.Task mTask;

    private LinkedList<ToiletCommand> mToiletCommandLinkedList;


    private static class SingletonHolder {
        private static final PooaiToiletCommandManager INSTANCE = new PooaiToiletCommandManager();
    }

    public static PooaiToiletCommandManager getInstance() {
        return PooaiToiletCommandManager.SingletonHolder.INSTANCE;
    }


    public PooaiToiletCommandManager() {
        mPooaiBleManager = PooaiBleManager.getInstance();
        mToiletCommandLinkedList = new LinkedList<>();
    }

    public void startHeartbeat() {
        if (mTask != null) {
            return;
        }
        mTask = TimerTaskUtil.intervalRx(0, 1000, new TimerTaskUtil.OnRxListener() {
            @Override
            public void onNext(Long aLong) {
                if (mToiletState == ToiletState.HEART) {
                    return;
                }
                if (mToiletCommandLinkedList.size() != 0) {
                    sendToiletCommand(mToiletCommandLinkedList.pollFirst());
                } else {
                    sendHeartbeatCommand();
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable disposable) {

            }
        });
    }

    private void sendHeartbeatCommand() {
        Log.d(TAG, "--发送心跳命令--");
        byte[] command = PooaiToiletDataUtil.getF03String(startAddress, SCAN_LENGTH);
        mPooaiBleManager.write(command);
    }

    private void sendToiletCommand(ToiletCommand toiletCommand) {
        Log.d(TAG, "--发送马桶命令--");
        byte[] command = PooaiToiletDataUtil.getF06String(toiletCommand.getCommandAddress(), toiletCommand.getCommandValue());
        mPooaiBleManager.write(command);
    }

    public void stopHeartbeat() {
        if (mTask != null && !mTask.isDisposed()) {
            mTask.cancel();
            mTask = null;
        }
    }

    public void addToiletCommand(@NonNull ToiletCommand toiletCommand) {
        mToiletCommandLinkedList.add(toiletCommand);
    }

    public void addToiletCommand(String cmd) {
        //直接发送byte数组 cmd.getBytes();
        mPooaiBleManager.write(cmd.getBytes());
    }

    public void changeToiletState(ToiletState toiletState) {
        if (mToiletState != toiletState) {
            ToiletCommandObservable.getInstance().setToiletState(toiletState);
            mToiletState = toiletState;
            switch (mToiletState) {
                case CONTROL:
                    startAddress = 50;
                    break;
                case DETECTION:
                    startAddress = 0;
                    break;
                case HEART:
                    break;
            }
        }
    }

    public ToiletState getToiletState() {
        return mToiletState;
    }
}
