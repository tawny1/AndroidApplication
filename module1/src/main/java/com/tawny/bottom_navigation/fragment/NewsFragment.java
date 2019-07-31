package com.tawny.bottom_navigation.fragment;

import android.util.Log;

import com.tawny.color_state_list.R;

/**
 * Author: tawny
 * Data：2019/6/12
 */
public class NewsFragment extends LazyFragment {

    public static NewsFragment newInstance() {
        return  new NewsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initData() {
        Log.e("TAG", "NewsFragment");
    }

    @Override
    protected void initView() {

    }
}
