package com.tawny.bottom_navigation.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author: tawny
 * Data：2019/6/13
 * <p>
 * fragment懒加载
 */
public abstract class LazyFragment extends Fragment {

    protected View mRootView;

    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            initView();
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirst) {
            initData();
            isFirst = false;
        }
        Log.e("TAG--LazyFragment", isFirst + "/...." + getClass() );
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void initView();

}
