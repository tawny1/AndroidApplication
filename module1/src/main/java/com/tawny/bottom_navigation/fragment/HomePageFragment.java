package com.tawny.bottom_navigation.fragment;

import android.util.Log;

import com.tawny.color_state_list.R;

/**
 * Author: tawny
 * Data：2019/6/12
 */
public class HomePageFragment extends LazyFragment {

    public static HomePageFragment newInstance() {
        return  new HomePageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_page;
    }

    @Override
    protected void initData() {
        Log.e("TAG", "HomePageFragment");
    }

    @Override
    protected void initView() {

    }

}
