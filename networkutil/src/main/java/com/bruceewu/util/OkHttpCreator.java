package com.bruceewu.util;

import java.io.IOException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp
 */

public class OkHttpCreator {
    public OkHttpClient create() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder = builder.followRedirects(true);
        builder = builder.followSslRedirects(true);
//        builder = builder.addNetworkInterceptor(new StethoInterceptor());     //TODO 什么情况, 注释也能继续工作啊?
        builder = builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                CookieHelper.instance().write(url, cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return CookieHelper.instance().read(url);
            }
        });

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new OkHttpUtils.EasyX509TrustManager(null)}, null);
            builder = builder.sslSocketFactory(sslContext.getSocketFactory());
            builder = builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        builder = builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(addHeader(chain));
            }
        });

        return builder.build();
    }

    protected Request addHeader(Interceptor.Chain chain) {
        Request request = chain.request().newBuilder()
                .addHeader("CLIENT_TYPE", "Android")
                .addHeader("CLIENT_VERSION", String.valueOf(17))
                .build();
        return request;
    }
}
