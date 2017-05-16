package com.bruceewu;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by mac on 2017/5/16.
 */

public enum ProtocolType {
    HOME_TABS("index", "tabs", Macro.UNUSE_HTTPS);


    private String act;
    private String op;
    private boolean https = false;

    ProtocolType(String act, String op, boolean https) {
        this.act = act;
        this.op = op;
        this.https = https;
    }

    public String act() {
        return act;
    }

    public String op() {
        return op;
    }

    public boolean https() {
        return https;
    }

    public HashMap<String, String> params() {
        HashMap<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(act())) {
            params.put("act", act());
        }
        if (!TextUtils.isEmpty(op())) {
            params.put("op", op());
        }
        return params;
    }
}