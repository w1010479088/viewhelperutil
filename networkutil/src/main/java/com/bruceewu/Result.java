package com.bruceewu;

import retrofit2.Call;

/**
 * Created by mac on 2017/5/16.
 */

public class Result {

    private ProtocolType mCurPtotocal;
    private int hashCode;

    public Result() {
    }

    public boolean needHandle(Call call) {
        if (call == null)
            return false;
        return this.hashCode == call.hashCode();
    }

    public void putValue(int hashCode, ProtocolType protocolType) {
        this.hashCode = hashCode;
        this.mCurPtotocal = protocolType;
    }

    public int dest_code() {
        return hashCode;
    }

    public ProtocolType protocol_type() {
        return mCurPtotocal;
    }

}
