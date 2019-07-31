package com.tawny.coordinator_layout.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 类说明：广告轮播图的ViewPager
 *
 * @author 马帅发
 * @date 2018/1/5
 */

public class BannerViewPager extends ViewPager {

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    int time = 6666;
    boolean isCarousel = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isCarousel) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopTimer();
                break;
            case MotionEvent.ACTION_UP:
                startTimer();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setCarousel(boolean carousel) {
        isCarousel = carousel;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void startTimer() {
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, time);
    }

    public void stopTimer() {
        handler.removeMessages(0);
    }

     Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                setCurrentItem(getCurrentItem() + 1);
                sendEmptyMessageDelayed(0, time);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null) {
            handler.removeMessages(0);
            handler = null;
        }
    }
}
