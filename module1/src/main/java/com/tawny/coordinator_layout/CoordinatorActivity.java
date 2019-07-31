package com.tawny.coordinator_layout;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tawny.color_state_list.R;
import com.tawny.coordinator_layout.widget.BannerView;
import java.util.ArrayList;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import static android.support.design.widget.TabLayout.MODE_FIXED;

/**
 * Author: tawny
 * Data：2019/6/12
 */
public class CoordinatorActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appBar;
    private RecyclerView hotGoods;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ImageView ivMenu;
    private TextView tvSearch;
    private BannerView bannerView;
    private ImageView bannerBg;
    private LinearLayout searchBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        hotGoods = findViewById(R.id.hotGoods);
        viewPager = findViewById(R.id.viewPager);
        appBar = findViewById(R.id.appBar);
        tabLayout = findViewById(R.id.tabLayout);
        ivMenu = findViewById(R.id.ivMenu);
        tvSearch = findViewById(R.id.tvSearch);
        bannerView = findViewById(R.id.bannerView);
        bannerBg = findViewById(R.id.banner_bg);
        searchBar = findViewById(R.id.toolbar);

        appBar.addOnOffsetChangedListener(this);

        hotGoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hotGoods.setAdapter(new CoordinatorActivity.HotGoodsAdapter());

        tabLayout.setTabMode(MODE_FIXED);


        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new CategoryAdapter());

        Glide.with(CoordinatorActivity.this).load(R.mipmap.b)
                //glide设置模糊
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(4, 10))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(bannerBg);

        final ArrayList<Bitmap> ss = new ArrayList<>();
        Glide.with(this.bannerView).asBitmap().load(R.mipmap.b)
                //glide设置圆角
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(10, 0)))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (ss.size() == 1) {
                            ss.add(0, resource);
                            initBannerView(ss);
                        } else {
                            ss.add(resource);
                        }
                    }
                });
        Glide.with(this.bannerView).asBitmap().load(R.mipmap.c)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(10, 0)))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ss.add(resource);
                        if (ss.size() == 2) {
                            initBannerView(ss);
                        }
                    }
                });
        bannerView.setOnPagerSelectedListener(pageIndex -> Glide.with(CoordinatorActivity.this).load(pageIndex == 0 ? R.mipmap.b : R.mipmap.c)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(4, 10))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(bannerBg));
    }

    private void initBannerView(ArrayList<Bitmap> ss) {
        bannerView.loadFromBmp(ss);
    }


    private ArgbEvaluator argbEvaluator;
    private boolean isDark;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float progress =
                (float) Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange() * 1.0f;
        Log.e("TAG", "progress: " + progress + "     verticalOffset: " + Math.abs(verticalOffset) + "      appBarLayout: " + appBarLayout.getTotalScrollRange() * 1.0f);
        if (argbEvaluator == null) {
            argbEvaluator = new ArgbEvaluator();
        }
        int bgEvaluateColor = (int) argbEvaluator.evaluate(1 - progress, Color.parseColor("#ffffffff"), Color.parseColor("#00ffffff"));
        searchBar.setBackgroundColor(bgEvaluateColor);
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (progress > 0.5 && !isDark) {
            isDark = true;
            ivMenu.getDrawable().setTint(Color.GRAY);
            GradientDrawable background = (GradientDrawable) tvSearch.getBackground().mutate();
            background.setStroke(dip2px(this, 1), Color.GRAY);
            tvSearch.getCompoundDrawables()[0].setTint(Color.GRAY);
            tvSearch.setTextColor(Color.GRAY);
        } else if (progress < 0.5 && isDark) {
            isDark = false;
            ivMenu.getDrawable().setTint(Color.parseColor("#ECEDEC"));
            GradientDrawable background = (GradientDrawable) tvSearch.getBackground().mutate();
            background.setStroke(dip2px(this, 1), Color.parseColor("#ECEDEC"));
            tvSearch.getCompoundDrawables()[0].setTint(Color.parseColor("#ECEDEC"));
            tvSearch.setTextColor(Color.parseColor("#ececec"));
        }
    }

    public static int dip2px(Context context, float dpValue) {
//        float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
        return Math.round(context.getResources().getDisplayMetrics().density * dpValue);
    }

    class HotGoodsAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(CoordinatorActivity.this).inflate(R.layout.item_hot_goods, viewGroup, false)) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }


    class CategoryAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            RecyclerView recyclerView = new RecyclerView(CoordinatorActivity.this);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setAdapter(new CoordinatorActivity.CategoryGoodsAdapter());
            container.addView(recyclerView, position);
            return recyclerView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            super.destroyItem(container, position, object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "热门推荐" : "猜你喜欢";
        }
    }

    class CategoryGoodsAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            if (position == 1) {
                return 1;
            }
            if (position == 2) {
                return 2;
            }
            return 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (i == 1) {
                View frame = LayoutInflater.from(CoordinatorActivity.this).inflate(R.layout.item_image, viewGroup, false);
                ImageView imageView = frame.findViewById(R.id.image);
                imageView.setImageResource(R.mipmap.tcbj);
                return new RecyclerView.ViewHolder(frame) {
                };
            } else if (i == 2) {
                View frame = LayoutInflater.from(CoordinatorActivity.this).inflate(R.layout.item_image, viewGroup, false);
                ImageView imageView = frame.findViewById(R.id.image);
                imageView.setImageResource(R.mipmap.top);
                return new RecyclerView.ViewHolder(frame) {
                };
            } else {
                return new RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_good, viewGroup, false)) {
                };
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

}