package com.pooai.blesdk.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 作者：created by xieying on 2020-01-12 18:26
 * 功能：
 */
public class PooaiToiletService extends Service {
    private static final String TAG = PooaiToiletService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PooaiServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class PooaiServiceBinder extends Binder {
        public PooaiToiletService getService() {
            return PooaiToiletService.this;
        }
    }
}
