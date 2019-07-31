package com.tawny.bottom_navigation.fragment;

import android.util.Log;

import com.tawny.color_state_list.R;

/**
 * Author: tawny
 * Data：2019/6/12
 */
public class SearchFragment extends LazyFragment {

    public static SearchFragment newInstance() {
        return  new SearchFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initData() {
        Log.e("TAG", "SearchFragment");
    }

    @Override
    protected void initView() {

    }

}
