package com.diskkiller.http.ssl;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 *    author : diskkiller
 *    time   : 2020/11/30
 *    desc   : Https 配置类
 */
public final class HttpSslConfig {

    private final SSLSocketFactory mSSLSocketFactory;
    private final X509TrustManager mTrustManager;

    HttpSslConfig(SSLSocketFactory factory, X509TrustManager manager) {
        mSSLSocketFactory = factory;
        mTrustManager = manager;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSSLSocketFactory;
    }

    public X509TrustManager getTrustManager() {
        return mTrustManager;
    }
}