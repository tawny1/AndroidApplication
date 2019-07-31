package com.tawny.bottom_navigation.fragment;

import android.util.Log;

import com.tawny.color_state_list.R;

/**
 * Author: tawny
 * Data：2019/6/12
 */
public class ExploreFragment extends LazyFragment {

    public static ExploreFragment newInstance() {
        return  new ExploreFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    protected void initData() {
        Log.e("TAG", "ExploreFragment");
    }

    @Override
    protected void initView() {

    }
}
