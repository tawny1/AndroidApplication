package com.tawny.bottom_navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.tawny.bottom_navigation.fragment.ExploreFragment;
import com.tawny.bottom_navigation.fragment.HomePageFragment;
import com.tawny.bottom_navigation.fragment.NewsFragment;
import com.tawny.bottom_navigation.fragment.SearchFragment;
import com.tawny.bottom_navigation.widget.AHBottomNavigation;

import com.tawny.bottom_navigation.widget.AHBottomNavigationViewPager;
import com.tawny.bottom_navigation.widget.CommonViewPagerAdapter;
import com.tawny.color_state_list.R;

/**
 * Author: tawny
 * Data：2019/6/12
 */
public class BottomNavigationActivity extends AppCompatActivity {

    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigation navBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        navBar = findViewById(R.id.nav_bar);
        viewPager = findViewById(R.id.view_pager);

        initBottomBar();
        initViewPager();
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(4);
        CommonViewPagerAdapter pagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment("Explore", ExploreFragment.newInstance());
        pagerAdapter.addFragment("News", NewsFragment.newInstance());
        pagerAdapter.addFragment("Search", SearchFragment.newInstance());
        pagerAdapter.addFragment("Personal", HomePageFragment.newInstance());
        viewPager.setAdapter(pagerAdapter);
    }


    private void initBottomBar() {
        AHBottomNavigationItem search = new AHBottomNavigationItem(R.string.search, R.mipmap.ic_search_white, R.color.blue);
        AHBottomNavigationItem profile = new AHBottomNavigationItem(R.string.profile, R.mipmap.ic_personal_page_white, R.color.brown);
        AHBottomNavigationItem explore = new AHBottomNavigationItem(R.string.explore, R.mipmap.ic_explore_white, R.color.purple);
        AHBottomNavigationItem news = new AHBottomNavigationItem(R.string.news, R.mipmap.ic_notifications_white, R.color.teal);
        navBar.addItem(explore);
        navBar.addItem(news);
        navBar.addItem(search);
        navBar.addItem(profile);
        navBar.setColored(false);
        navBar.setOnTabSelectedListener((position, wasSelected) -> {
                viewPager.setCurrentItem(position, false);
            return true;
        });
    }
}
