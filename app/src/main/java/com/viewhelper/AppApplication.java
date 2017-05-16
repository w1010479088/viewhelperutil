package com.viewhelper;

import android.app.Application;

import com.bruceewu.util.CookieHelper;

/**
 * Created by mac on 2017/5/16.
 */

public class AppApplication extends Application {
    private static AppApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        CookieHelper.onApplicationCreate(this);
    }

    public static AppApplication instance() {
        return instance;
    }
}
