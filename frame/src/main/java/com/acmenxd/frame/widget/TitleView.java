package com.acmenxd.frame.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.acmenxd.frame.R;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/5 15:15
 * @detail 顶部标题栏
 */
public class TitleView extends FrameLayout {
    private FrameLayout mBack;
    private TextView mTitle;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(LayoutInflater.from(context).inflate(R.layout.widget_title, null));
        mBack = (FrameLayout) findViewById(R.id.title_fl_back);
        mTitle = (TextView) findViewById(R.id.title_tv_title);
    }

    public void showThis() {
        this.setVisibility(VISIBLE);
    }

    public void hideThis() {
        this.setVisibility(GONE);
    }

    public void showBack() {
        mBack.setVisibility(VISIBLE);
    }

    public void hideBack() {
        mBack.setVisibility(GONE);
    }

    public void showTitle() {
        mTitle.setVisibility(VISIBLE);
    }

    public void hideTitle() {
        mTitle.setVisibility(GONE);
    }

    public void setTitle(String pTitleStr) {
        mTitle.setText(pTitleStr);
    }

    public void setBackLisener(OnClickListener pListener) {
        mBack.setOnClickListener(pListener);
    }
}
