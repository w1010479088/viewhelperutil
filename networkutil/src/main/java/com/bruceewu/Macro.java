package com.bruceewu;

/**
 * Created by mac on 2017/5/16.
 */

public class Macro {
    public static final boolean USE_HTTPS = false;
    public static final boolean UNUSE_HTTPS = false;
    private static volatile Macro instance;

    private Macro() {
    }

    public static Macro instance() {
        if (instance == null) {
            synchronized (Macro.class) {
                if (instance == null) {
                    instance = new Macro();
                }
            }
        }
        return instance;
    }

    public String host_url(boolean fhttps) {
        return "http://a.lrlz.com";
    }
}
