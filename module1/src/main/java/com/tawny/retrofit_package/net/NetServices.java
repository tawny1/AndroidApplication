package com.tawny.retrofit_package.net;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Author: tawny
 * Data：2019/6/15
 */
public interface NetServices {

    @GET("/")
    Observable<Object> getUserInfo();
}
