package com.pooai.blesdk;

import android.content.Context;

/**
 * 作者：created by xieying on 2020-01-12 15:31
 * 功能：
 */
public class AppEvent {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }


    public static Context getContext() {
        return mContext;
    }
}
