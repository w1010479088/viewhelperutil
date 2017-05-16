package com.bruceewu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Method;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mac on 2017/5/16.
 */

public class ResponseHandler<RetType, DataType> implements Callback<String> {
    protected Class<RetType> mRetType;
    protected Class<DataType> mDataType;
    protected ProtocolType mCurProtocal;
    protected int mHashCode;

    public ResponseHandler(Class<RetType> retType, ProtocolType protocal, int hashCode) {
        mRetType = retType;
        mDataType = null;
        mCurProtocal = protocal;
        mHashCode = hashCode;
    }

    public ResponseHandler(Class<RetType> retType, Class<DataType> dataType, ProtocolType protocal, int hashCode) {
        mRetType = retType;
        mDataType = dataType;
        mCurProtocal = protocal;
        mHashCode = hashCode;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> resp) {
        if (resp.isSuccessful()) {
            try {
                Gson gson = new Gson();
                JsonObject obj = gson.fromJson(resp.body(), JsonObject.class);

                if (obj == null) {
                    postError(mHashCode, null, new Exception("数据异常"));
                } else {
                    int code = gson.fromJson(obj.get("code"), int.class);
                    if (code == 200) {
                        postSuccess(mHashCode, obj);
                    } else {
                        postError(mHashCode, resp, null);
                    }
                }
            } catch (Exception ex) {
                postError(mHashCode, resp, null);
            }
        } else {
            postError(mHashCode, resp, null);
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        postError(mHashCode, null, t);
    }


    protected void postSuccess(int hashCode, JsonObject obj) {
        try {
            RetType ret;
            Gson gson = new Gson();
            if (mDataType == null) {
                ret = gson.fromJson(obj.get("datas"), mRetType);
                if (ret == null) {
                    ret = mRetType.newInstance();
                }
            } else {
                ret = mRetType.newInstance();
                DataType data = gson.fromJson(obj.get("datas"), mDataType);
                Method method = mRetType.getMethod("put_data", mDataType);
                method.invoke(ret, data);
            }
            Method method = mRetType.getMethod("putValue", int.class, ProtocolType.class);
            method.invoke(ret, hashCode, mCurProtocal);
            post(ret);
        } catch (Exception e) {
            postError(hashCode, null, e);
        }
    }

    protected void postError(int hashCode, Response<String> resp, Throwable e) {
        RetTypes.Error error = new RetTypes.Error();
        error.setHashCode(hashCode);
        try {
            if (resp != null) {
                if (!resp.isSuccessful()) {
                    error.setCode(resp.code());
                    error.setErrorMsg(resp.message());
                } else {
                    Gson gson = new Gson();
                    JsonObject obj = gson.fromJson(resp.body(), JsonObject.class);
                    int code = gson.fromJson(obj.get("code"), int.class);
                    String msg = gson.fromJson(obj.get("message"), String.class);
                    error.setCode(code);
                    error.setErrorMsg(msg);
                }

            } else {
                error.setCode(RetTypes.Error.CODE_UNKONWN);
                error.setErrorMsg("网络错误!");
            }

            if (error.getCode() == RetTypes.Error.CODE_UNLOGIN) {
                handleUnLogin();
            }
            post(error);
        } catch (Exception ex) {
            error.setCode(RetTypes.Error.CODE_UNKONWN);
            error.setErrorMsg("网络错误!");
            post(error);
        }
    }

    protected void handleUnLogin() {

    }

    protected void post(Object obj) {

    }
}
