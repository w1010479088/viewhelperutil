package com.bruceewu;

import com.bruceewu.util.GsonConverterFactory;
import com.bruceewu.util.OkHttpUtils;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by mac on 2017/5/16.
 */
class RequestUtil {
    private static Retrofit createRetrofit(String host) {
        OkHttpClient httpclient = OkHttpUtils.instance().getHttpclient();

        return new Retrofit.Builder().baseUrl(host).client(httpclient).addConverterFactory(GsonConverterFactory.create()).build();
    }

    protected static String get_host(boolean fhttps) {
        return Macro.instance().host_url(fhttps);
    }

    protected static <T> Call<String> get(HashMap<String, String> params, Class<T> type, ProtocolType protocol) {
        boolean fhttps = protocol.https();
        Retrofit rf = createRetrofit(get_host(fhttps));
        RestApiEx service = rf.create(RestApiEx.class);

        Call<String> call = service.get(params);
        call.enqueue(new ResponseHandler<>(type, protocol, call.hashCode()));
        return call;
    }

//        private static String get_wxhost() {
//            return Macro.URL_WX_SERVER_HOST;
//        }
//
//        private static <T> RequestorActivity.LoginCheckCall<String> getLoginCheckCall(HashMap<String, String> params, Class<T> type, ProtocolType protocol) {
//            boolean fhttps = protocol.https();
//            Retrofit rf = createRetrofit(get_host(fhttps));
//            RestApiEx service = rf.create(RestApiEx.class);
//            Call<String> call = service.get(params);
//            return new RequestorActivity.LoginCheckCall(call,new ResponseHandler<>(type, protocol, call.hashCode()));
//        }
//
//        //同步
//        private static <T> void getExecute(HashMap<String, String> params, Class<T> clazz, ProtocolType protocol, Http.HttpCallBack<T> callBack) {
//            boolean fhttps = protocol.https();
//            Retrofit rf = createRetrofit(get_host(fhttps));
//            RestApiEx service = rf.create(RestApiEx.class);
//            Http.executeHandler(callBack, clazz, service.get(params));
//        }
//
//        //异步
//        @SuppressWarnings("unused")
//        private static <T> Call<String> getEnqueue(HashMap<String, String> params, Class<T> clazz, ProtocolType protocol, Http.HttpCallBack<T> callBack) {
//            boolean fhttps = protocol.https();
//            Retrofit rf = createRetrofit(get_host(fhttps));
//            RestApiEx service = rf.create(RestApiEx.class);
//            return Http.enqueueHandler(callBack, clazz, service.get(params));
//        }
//
//        //同步上传
//        private static <T> void getExecute(File file, Class<T> clazz, ProtocolType protocol, Http.HttpCallBack<T> callBack) {
//            boolean fhttps = protocol.https();
//            Retrofit rf = createRetrofit(get_host(fhttps));
//            RestApiEx service = rf.create(RestApiEx.class);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            Call<String> call = service.uploadImage("Upload", requestBody);
//            Http.executeHandler(callBack, clazz, call);
//        }
//
//        private static <T> Call<String> post(HashMap<String, String> params, HashMap<String, String> bodyMap, Class<T> type, ProtocolType protocol) {
//            boolean fhttps = protocol.https();
//            Retrofit rf = createRetrofit(get_host(fhttps));
//            RestApiEx service = rf.create(RestApiEx.class);
//
//            Call<String> call = service.post(params, bodyMap);
//            call.enqueue(new ResponseHandler<>(type, protocol, call.hashCode()));
//            return call;
//        }
//
//        private static <T> Call<String> wxrequest(HashMap<String, String> params, Class<T> type, ProtocolType protocol) {
//            Retrofit rf = createRetrofit(get_wxhost());
//            RestApiEx service = rf.create(RestApiEx.class);
//
//            Call<String> call = null;
//            switch (protocol) {
//                case WX_ACESSTOKEN:
//                    call = service.wxAccessToken(params);
//                    break;
//                case WX_USERINFO:
//                    call = service.wxUserinfo(params);
//                    break;
//            }
//            if (call != null) {
//                call.enqueue(new NormalHandler<>(type, protocol, call.hashCode()));
//            }
//            return call;
//        }
//
//        //上传文件
//        private static <T> Call<String> request(File icon, Class<T> type, ProtocolType protocol) {
//            boolean fhttps = protocol.https();
//            Retrofit rf = createRetrofit(get_host(fhttps));
//            RestApiEx service = rf.create(RestApiEx.class);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), icon);
//            Call<String> call = service.uploadImage("Upload", requestBody);
//            call.enqueue(new ResponseHandler<>(type, protocol, call.hashCode()));
//            return call;
//        }
//
//        private static <RetType, DataType> Call<String> request(HashMap<String, String> params,
//                                                                Class<RetType> retType,
//                                                                Class<DataType> dataType,
//                                                                ProtocolType protocol) {
//            boolean fhttps = protocol.https();
//            Retrofit rf = createRetrofit(get_host(fhttps));
//            RestApiEx service = rf.create(RestApiEx.class);
//            Call<String> call = service.get(params);
//
//            call.enqueue(new ResponseHandler<>(retType, dataType, protocol, call.hashCode()));
//            return call;
//        }
}
