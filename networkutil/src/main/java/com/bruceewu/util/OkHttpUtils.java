package com.bruceewu.util;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * OkHttpUtils的Cookies管理
 */
public class OkHttpUtils {
    private static class InstanceHolder {
        private static final OkHttpUtils INSTANCE = new OkHttpUtils();
    }

    public static OkHttpUtils instance() {
        return InstanceHolder.INSTANCE;
    }

    private OkHttpClient mHttpclient;

    private OkHttpUtils() {
        mHttpclient = okHttpClient();
    }

    //可以重写OkHttpCreator
    protected OkHttpClient okHttpClient(){
        return new OkHttpCreator().create();
    }

    public OkHttpClient getHttpclient() {
        return mHttpclient;
    }

    public static class EasyX509TrustManager implements X509TrustManager {
        private X509TrustManager standardTrustManager = null;

        EasyX509TrustManager(KeyStore keystore)
                throws NoSuchAlgorithmException, KeyStoreException {
            super();
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(keystore);
            TrustManager[] trustmanagers = factory.getTrustManagers();
            if (trustmanagers.length == 0) {
                throw new NoSuchAlgorithmException("no trust manager found");
            }
            this.standardTrustManager = (X509TrustManager) trustmanagers[0];
        }

        public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
            standardTrustManager.checkClientTrusted(certificates, authType);
        }

        public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
            if ((certificates != null) && (certificates.length == 1)) {
                certificates[0].checkValidity();
            } else {
                standardTrustManager.checkServerTrusted(certificates, authType);
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return this.standardTrustManager.getAcceptedIssuers();
        }
    }
}
