package com.pooai.pooaisdk;

import android.app.Application;

import com.pooai.blesdk.AppEvent;

/**
 * 作者：created by xieying on 2020-01-18 18:10
 * 功能：
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppEvent.init(this);
    }
}
