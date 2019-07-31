package com.tawny.coordinator_layout.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tawny.color_state_list.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 类说明：广告轮播图的设置
 *
 * @date 2018/2/2
 */

public class BannerView extends RelativeLayout implements View.OnTouchListener, ViewPager.OnPageChangeListener {

    BannerViewPager vp;

    private ArrayList<Bitmap> bitmapList;

    private int timeInterval = 6666;
    private int maxCount = 600000;
    private float margins, vpWidth, pagerInterval;
    /**
     * 是否需要测量图片宽度同比缩放设置高度
     */
    private boolean isMeasure = true;
    /**
     * 是否需要轮播
     */
    private boolean isCarousel = true;
    /**
     * 是否有指示器
     */
    private boolean displayIndicator;
    /**
     * 选中指示器的图片id
     */
    private @DrawableRes
    int selectedIndicator;
    /**
     * 未选择指示器的图片id
     */
    private @DrawableRes
    int unselectedIndicator;
    /**
     * 指示器布局
     */
    private LinearLayout llIndicator;
    /**
     * 指示器布局父布局，在相对布局中设置位于父布局下方会导致相对布局全屏(需定死高度)，
     * 但又不好直接设置最此控件的高度，因为不知道使用这个控件的父布局是什么，
     * 所以在此控件跟指示器布局中间加了一次以限制高度
     */
    private RelativeLayout rlIndicatorParent;
    /**
     * 每个指示器
     */
    private ArrayList<ImageView> listTab;
    /**
     * 图片圆角度数
     */
    private int imgRadius;
    private PagerAdapter adapter;


    public BannerView(Context context) {
        super(context);
        init(null);
    }


    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_banner, this);
        vp = findViewById(R.id.vp);
        if (attrs != null) {
            @SuppressLint("Recycle") TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);
            timeInterval = typedArray.getInteger(R.styleable.BannerView_vp_time_interval, 6666);
            pagerInterval = typedArray.getDimension(R.styleable.BannerView_vp_pager_interval, 0);
            margins = typedArray.getDimension(R.styleable.BannerView_vp_margins, 0);
            vpWidth = typedArray.getDimension(R.styleable.BannerView_vp_width, 0);
            isMeasure = typedArray.getBoolean(R.styleable.BannerView_vp_is_measure, true);
            isCarousel = typedArray.getBoolean(R.styleable.BannerView_vp_is_carousel, true);
            displayIndicator = typedArray.getBoolean(R.styleable.BannerView_display_indicator, false);
            selectedIndicator = typedArray.getResourceId(R.styleable.BannerView_selected_indicator, -1);
            unselectedIndicator = typedArray.getResourceId(R.styleable.BannerView_unselected_indicator, -1);
            imgRadius = typedArray.getInt(R.styleable.BannerView_corners_radius, -1);
            typedArray.recycle();
        }
    }

    private int progress;

    public void loadFromUrl(final ArrayList<String> urlList) {
        if (urlList == null || urlList.size() < 1) {
            return;
        }
        progress = 0;
        bitmapList = new ArrayList<>();
        final HashMap<String, Bitmap> map = new HashMap<>();
        for (int i = 0; i < urlList.size(); i++) {
            RequestBuilder<Bitmap> builder;
            if (imgRadius > 0) {
                builder = Glide.with(getContext()).asBitmap().apply(RequestOptions.centerCropTransform());
            } else {
                builder = Glide.with(getContext()).asBitmap();
            }
            builder.load(urlList.get(i).trim()).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                    progress++;     //加载失败回调
                    map.put((String) o, BitmapFactory.decodeResource(getResources(), R.mipmap.img_placeholder));
                    if (progress == urlList.size()) {
                        sortBitmapList(map, urlList);
                        initView();
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                    progress++;
                    map.put((String) o, bitmap);
                    if (progress == urlList.size()) {
                        sortBitmapList(map, urlList);    //异步加载，顺序不确定，所有图片下载完后，按传进来url的顺序再存进去
                        initView();       //图片加载时异步的，需等加载完毕再去初始化viewPager
                    }
                    return true;
                }
            }).submit();
        }
    }

    private void sortBitmapList(HashMap<String, Bitmap> map, List<String> links) {
        for (String link : links) {
            bitmapList.add(map.get(link));
        }
    }

    public void loadFromResources(@DrawableRes int... imgs) {
        if (imgs == null || imgs.length < 1) {
            return;
        }
        bitmapList = new ArrayList<>();
        for (int img : imgs) {
            bitmapList.add(BitmapFactory.decodeResource(getContext().getResources(), img));
        }
        initView();
    }

    public void loadFromBmp(ArrayList<Bitmap> ivList) {
        if (ivList == null || ivList.size() < 1) {
            return;
        }
        bitmapList = ivList;
        initView();
    }


    private void initView() {
        if (onImgLoadFinishListener != null) {
            onImgLoadFinishListener.onImgFinish();
        }
        if (displayIndicator && bitmapList.size() > 1) {
            listTab = new ArrayList<>();
            rlIndicatorParent = new RelativeLayout(getContext());
            llIndicator = new LinearLayout(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            llIndicator.setLayoutParams(params);
            llIndicator.setPadding(0, 0, 0, dip2px(getContext(), 15));
            int size = bitmapList.size();
            for (int i = 0; i < size; i++) {
                ImageView iv = new ImageView(getContext());
                iv.setPadding(dip2px(getContext(), 6), 0, dip2px(getContext(), 6), 0);
                iv.setImageResource(unselectedIndicator);
                listTab.add(iv);
                llIndicator.addView(iv);
            }
            for (int i = 0; i < getChildCount(); i++) {
                if (i > 0) {
                    removeViewAt(i);
                }
            }
            listTab.get(0).setImageResource(selectedIndicator);
            rlIndicatorParent.addView(llIndicator);
            addView(rlIndicatorParent);
            vp.addOnPageChangeListener(this);
        }
        initVp();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initVp() {
        if (adapter != null) {
            vp.stopTimer();
            vp.removeAllViews();
        } else {
            setOnTouchListener(this);
        }
        float picShowHeight = 0;
        if (isMeasure) {
            picShowHeight = getPicShowHeight();
        }
        if (displayIndicator && picShowHeight > 0) {
            rlIndicatorParent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) picShowHeight));
        }
        //高度按照宽度缩放比例同等缩放        ViewPager包裹内容没有用，必须固定
        LayoutParams params = new LayoutParams(vpWidth != 0 ? (int) vpWidth : LayoutParams.MATCH_PARENT, isMeasure ? (int) picShowHeight : LayoutParams.WRAP_CONTENT);
        if (margins != 0 || vpWidth != 0) {
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins((int) margins, 0, (int) margins, 0);
            if (bitmapList.size() > 1) {
                //如果左右有显示另外两页的话左右两边缓存2页
                vp.setOffscreenPageLimit(2);
            }
        }
        if (bitmapList.size() <= 1) {
            vp.setCarousel(false);
        }
        vp.setLayoutParams(params);
        //pager间间隔
        vp.setPageMargin((int) pagerInterval);
        if (adapter == null) {
            adapter = new PagerAdapter(bitmapList);
            vp.setAdapter(adapter);
            if (bitmapList.size() > 1 && isCarousel) {
                vp.setCurrentItem(maxCount / 1000, false);
            }
        } else {
            adapter.setDate(bitmapList);
            if (bitmapList.size() > 1 && isCarousel) {
                //数值越大越卡顿
                vp.setCurrentItem(vp.getCurrentItem() + 6, false);
            }
        }
        if (bitmapList.size() > 1 && isCarousel) {
            //轮播间隔
            vp.setTime(timeInterval);
            //开启轮播
            vp.startTimer();
        } else {
            vp.setCarousel(false);
        }
        //上面属性值如果用set方式必须在load前set才有用
    }

    /**
     * 算出按宽度同比缩放后的高度
     *
     * @return
     */
    public float getPicShowHeight() {
        //图片实际高
        float height = 0;
        //图片实际宽     图片实际高宽取得是图片中最高的一张
        float width = 0;
        //最终显示到屏幕的宽
        float finalWidth;
        float widthPix = getContext().getResources().getDisplayMetrics().widthPixels;
        for (Bitmap bitmap : bitmapList) {
            int h = bitmap.getHeight();
            if (h > height) {
                height = h;
                width = bitmap.getWidth();
            }
        }
        float pi = margins != 0 || vpWidth != 0 ? pagerInterval * 2 : pagerInterval;
        if (margins != 0 || vpWidth != 0) {
            if (vpWidth != 0 && widthPix - vpWidth > margins * 2) {
                finalWidth = vpWidth + pi;
            } else {
                finalWidth = widthPix - margins * 2 + pi;
            }
        } else {
            finalWidth = widthPix + pi;
        }
        float ration = width / finalWidth;
        return height / ration;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        vp.dispatchTouchEvent(event);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int index;

    @Override
    public void onPageSelected(int position) {
        if (displayIndicator) {
            int index = position % bitmapList.size();
            listTab.get(this.index).setImageResource(unselectedIndicator);
            listTab.get(index).setImageResource(selectedIndicator);
            this.index = index;
        }
        if (onPagerSelectedListener != null) {
            onPagerSelectedListener.onPagerSelected(position % bitmapList.size());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class PagerAdapter extends android.support.v4.view.PagerAdapter implements OnClickListener, OnLongClickListener {

        //如果存放源数据的list对象发生改变而没有立即调用notifyDataSetChanged会立即抛出异常
        ArrayList<Bitmap> bitmapList = new ArrayList<>();

        public PagerAdapter(ArrayList<Bitmap> bitmapList) {
            this.bitmapList.addAll(bitmapList);
        }

        public void setDate(ArrayList<Bitmap> bitmapList) {
            this.bitmapList.clear();
            this.bitmapList.addAll(bitmapList);
            this.notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(bitmapList.get(position % bitmapList.size()));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            if (onItemClickListener != null) {
                imageView.setTag(position % bitmapList.size());
                imageView.setOnClickListener(this);
            }
            if (onItemLongClickListener != null) {
                imageView.setTag(position % bitmapList.size());
                imageView.setOnLongClickListener(this);
            }
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if (bitmapList.size() == 1 || !isCarousel) {
                return bitmapList.size();
            }
            return maxCount;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            //如果item的位置没有发生改变则返回POSITION_UNCHANGED，。如果返回了POSITION_NONE，表示该位置的item已经不存在了。默认的实现是假设item的位置永远不会发生变化，而返回POSITION_UNCHANGED
            //只有一个item永远返回而返回POSITION_UNCHANGED，不会去重绘。所以重写，notifyDataSetChanged才用去重新调用instantiateItem
            if (bitmapList.size() == 1) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick((Integer) v.getTag());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick((Integer) v.getTag());
                return true;
            }
            return false;
        }
    }

    public void startCarousel() {
        if (vp != null && vp.isCarousel) {
            vp.startTimer();
        }
    }

    public void stopCarousel() {
        if (vp != null) {
            vp.stopTimer();
        }
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setCarousel(boolean carousel) {
        isCarousel = carousel;
    }

    public void setDisplayIndicator(boolean displayIndicator) {
        this.displayIndicator = displayIndicator;
        if (!displayIndicator && llIndicator != null) {
            llIndicator.setVisibility(View.GONE);
        }
    }

    public void setSelectedIndicator(int selectedIndicator) {
        this.selectedIndicator = selectedIndicator;
    }

    public void setUnselectedIndicator(int unselectedIndicator) {
        this.unselectedIndicator = unselectedIndicator;
    }

    public void setMargins(int margins) {
        this.margins = margins;
    }

    public void setVpWidth(int vpWidth) {
        this.vpWidth = vpWidth;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnImgLoadFinishListener(OnImgLoadFinishListener onImgLoadFinishListener) {
        this.onImgLoadFinishListener = onImgLoadFinishListener;
    }

    public void setOnPagerSelectedListener(OnPagerSelectedListener onPagerSelectedListener) {
        this.onPagerSelectedListener = onPagerSelectedListener;
    }

    private OnItemLongClickListener onItemLongClickListener;

    private OnItemClickListener onItemClickListener;

    private OnImgLoadFinishListener onImgLoadFinishListener;

    private OnPagerSelectedListener onPagerSelectedListener;

    /**
     * todo 注意！！  如果要显示出上下两页图的效果，点击事件将不可靠，中间的上一页点击事件拿到的下标会是中间iv的下标，暂时解决不了...(莫名)； 可以考虑在touch事件分发里算出点了哪页
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public interface OnPagerSelectedListener {
        void onPagerSelected(int pageIndex);
    }

    /**
     * 图片加载完成的回调，主要用于监听网络图片加载完毕
     */
    public interface OnImgLoadFinishListener {
        void onImgFinish();
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
