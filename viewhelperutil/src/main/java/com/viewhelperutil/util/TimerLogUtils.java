package com.viewhelperutil.util;

import android.util.Log;

/**
 *记录时间
 */

public class TimerLogUtils {
    private static volatile TimerLogUtils instance;
    private long lastLogTime;
    private String tag;

    private TimerLogUtils(String tag) {
        this.tag = tag;
    }

    public static TimerLogUtils getSingleTon(String tag) {
        if(instance == null){
            synchronized (TimerLogUtils.class){
                if(instance == null) {
                    instance = new TimerLogUtils(tag);
                }
            }
        }
        return instance;
    }

    public void start() {
        lastLogTime = System.currentTimeMillis();
    }

    public long end() {
        long endTime = System.currentTimeMillis();
        long lastedTime = endTime - lastLogTime;
        lastLogTime = endTime;
        Log.d(tag, "------" + lastedTime / 1000 + "------");
        return lastedTime;
    }
}
