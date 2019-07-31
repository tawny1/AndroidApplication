package com.tawny.color_state_list;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Author: tawny
 * Data：2019/6/12
 */
public class ColorStateListActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_state_list);
        textView = findViewById(R.id.textView);
        textView.setTextColor(createColorStateList(Color.parseColor("#000000"), 0xffffff00, 0xff0000ff, 0xffff0000));
        textView.setOnClickListener(this);
        textView.setFocusable(true);
    }

    /**
     * 对TextView设置不同状态时其文字颜色。
     */
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    @Override
    public void onClick(View v) {
        if (v == textView) {
            textView.setEnabled(false);
        }
    }
}
