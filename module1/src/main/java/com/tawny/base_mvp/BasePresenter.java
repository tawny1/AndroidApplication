package com.tawny.base_mvp;

import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * Author: tawny
 * Data：2019/7/4
 */
public abstract class BasePresenter<T> {

    //View接口类型的软引用
    protected Reference<T> mViewRef;

    public void attachView(T view) {
        //建立关系
        mViewRef = new SoftReference<>(view);
    }

    protected T getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
        }
    }
}
