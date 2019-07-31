package com.tawny;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tawny.bottom_navigation.BottomNavigationActivity;
import com.tawny.cardview_color.CardViewActivity;
import com.tawny.color_state_list.ColorStateListActivity;
import com.tawny.color_state_list.R;
import com.tawny.coordinator_layout.CoordinatorActivity;

/**
 * Author: tawny
 * Data：2019/6/12
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void gotoAct(Context context, Class clz) {
        Intent intent = new Intent(context, clz);
        startActivity(intent);
    }

    public void btn1(View view) {
        gotoAct(this, CardViewActivity.class);
    }

    public void btn2(View view) {
        gotoAct(this, BottomNavigationActivity.class);
    }

    public void btn3(View view) {
        gotoAct(this, ColorStateListActivity.class);
    }

    public void btn4(View view) {
        gotoAct(this, CoordinatorActivity.class);
    }
}
