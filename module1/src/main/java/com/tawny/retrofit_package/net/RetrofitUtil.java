package com.tawny.retrofit_package.net;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Timeout;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: tawny
 * Data：2019/6/15
 */
public class RetrofitUtil {

    public static final int DEFAULT_TIMEOUT = 8;

    private static final RetrofitUtil instance = new RetrofitUtil();

    public static RetrofitUtil getInstance() {
        return instance;
    }


    @SuppressLint("CheckResult")
    public static  <T>Observable<T> askNet(Observable<T> observable) {
        observable = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public NetServices getServices() {
        return getRetrofit().create(NetServices.class);
    }


    Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://www.ifeng.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
    }



    private OkHttpClient getClient() {

        //缓存容量     缓存路径
        long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MiB
        String cacheFile = "/http";
        Cache cache = new Cache(new File(cacheFile), SIZE_OF_CACHE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
                .cache(cache)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new CacheInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Log.e("TAG--", "....");
                        return chain.proceed(chain.request());
                    }
                })
                .addNetworkInterceptor(new CacheInterceptor());



        return builder.build();
    }

    private OkHttpClient.Builder okHttpClientBuilder =

            new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(chain -> {

                        Request originalRequest = chain.request();

                        HttpUrl originalHttpUrl = originalRequest.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .build();

                        FormBody.Builder bodyBuilder = new FormBody.Builder();
                        FormBody formBody;
                        if (originalRequest.body() instanceof FormBody) {
                            formBody = (FormBody) originalRequest.body();
                        } else {
                            formBody = bodyBuilder.build();
                        }

                        String time = System.currentTimeMillis() / 1000 + "";
                        TreeMap<String, String> map = new TreeMap();
                        map.put("timestamp", time);
                        //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
                        for (int i = 0; i < formBody.size(); i++) {
                            bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                            map.put(formBody.encodedName(i), formBody.encodedValue(i));
                        }

                        StringBuilder sign = new StringBuilder();

                        if (map.size() > 0) {
                            for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                                Map.Entry entry = stringStringEntry;
                                String key = (String) entry.getKey();
                                String val = (String) entry.getValue();
                                if (!TextUtils.isEmpty(val)) {
                                    sign.append(key).append("=").append(val).append("&");
                                }
                            }
                        }
//                        formBody = bodyBuilder
//                                .addEncoded("sign", Md5Util.md5(sign.toString()))
//                                .addEncoded("timestamp", time)
//                                .build();

                        Request request;
                        Request.Builder method = originalRequest.newBuilder()
                                .url(url)
                                .post(formBody)
                                .method(originalRequest.method(), formBody);

//                        method.addHeader(Const.MODEL, Build.MODEL);
//                        method.addHeader(Const.ANDROID_ID, DeviceInfoUtils.getInstance().getAndroidId());
//                        method.addHeader(Const.IMEI, DeviceInfoUtils.getInstance().getIMEI());
//                        method.addHeader(Const.MAC, DeviceInfoUtils.getInstance().getMacAddress());
//                        method.addHeader(Const.APP_VERSION_CODE, DeviceInfoUtils.getInstance().getAppVersionCode());
//                        method.addHeader(Const.APP_VERSION_NAME, DeviceInfoUtils.getInstance().getappVersionName());
//                        method.addHeader(Const.LANGUAGE, DeviceInfoUtils.getInstance().getSystemLanguage());
//                        method.addHeader(Const.PACKAGE, DeviceInfoUtils.getInstance().getPackageName());
//                        method.addHeader(Const.APP_NAME, "myt_tools");
//                        method.addHeader(Const.APP_OS, "android");
//                        if (!TextUtils.isEmpty(MyApp.getInstance().getAppToken())) {
//                            method.addHeader(Const.TOKEN, MyApp.getInstance().getAppToken());
//                        }

                        request = method.build();
                        return chain.proceed(request);
                    });


    @SuppressLint("TrulyRandom")
    public SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return sSLSocketFactory;
    }

    public static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
