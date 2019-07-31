package com.tawny.retrofit_package.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: tawny
 * Data：2019/6/15
 */
public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String cacheTime = request.header("cache");

        if (cacheTime == null) {
            cacheTime = "0";
        }
        int requestCacheTime = 0;
        try {
            requestCacheTime = Integer.parseInt(cacheTime);
        } catch (Exception e) {
            Log.e("CacheInterceptor", "cacheTime 时间设置不对");
        }

//       request = new Request.Builder()
//                .url("http://www.ifeng.com")
//                .cacheControl(new CacheControl.Builder().noStore().build())
//                .build();
//
//
//        Response re = chain.proceed(request);
//        Log.e("TAG-->networkResponse:", re.networkResponse() + "");
//        Log.e("TAG-->cache:", re.cacheResponse() + "");



//        return re;

        if (NetworkUtils.isConnected()) {
            //有网时
            Response response = chain.proceed(request);
            if (requestCacheTime == 0) {
                return response;
            }
            Log.e("CacheInterceptor", "在线缓存在" + cacheTime + "秒内可读取");
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + cacheTime)
                    .build();
        } else {
            //无网时
            //有缓存数据一直从缓存中取出
            if (requestCacheTime == 0) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)//此处不设置过期时间
                        .build();
            }
            //有缓存数据一直从缓存中取出，但是要判断缓存是否过期  504 Unsatisfiable Request
            else {
                CacheControl FORCE_CACHE1 = new CacheControl.Builder()
                        .onlyIfCached()
                        .maxAge(requestCacheTime, TimeUnit.SECONDS) //设置缓存的时间  ，判断缓存是否过期
//                                .maxStale(60, TimeUnit.SECONDS)//CacheControl.FORCE_CACHE--是int型最大值
                        .build();
                request = request.newBuilder()
                        .cacheControl(FORCE_CACHE1)//此处设置了t秒---修改了系统方法
                        .build();
            }
            Response response = chain.proceed(request);
            return response;
        }
    }
}
