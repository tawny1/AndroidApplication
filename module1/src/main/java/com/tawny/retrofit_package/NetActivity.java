package com.tawny.retrofit_package;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tawny.color_state_list.R;
import com.tawny.retrofit_package.net.BaseResponse;
import com.tawny.retrofit_package.net.RetrofitUtil;
import com.tawny.retrofit_package.net.UserBean;

import io.reactivex.functions.Consumer;

/**
 * Author: tawny
 * Data：2019/6/15
 */
public class NetActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RetrofitUtil.getInstance().askNet(null).getServices().getUserInfo());

//        RetrofitUtil.getInstance().askNet().getServices();

        RetrofitUtil.askNet(RetrofitUtil.getInstance().getServices().getUserInfo()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }
}
