package com.acmenxd.appbase.utils;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.acmenxd.frame.utils.Utils;
import com.acmenxd.appbase.R;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/15 15:10
 * @detail Title工具类
 */
public final class TitleUtils {
    public abstract static class OnTitleListener {
        public void onBack() {
        }

        public void onTitle() {
        }

        public void onRightTitle() {
        }
    }

    public static void initTitleView(View view, final OnTitleListener pListener) {
        initTitleView(view, R.drawable.left, "", "", pListener);
    }

    public static void initTitleView(View view, String title, final OnTitleListener pListener) {
        initTitleView(view, R.drawable.left, title, "", pListener);
    }

    public static void initTitleView(View view, String title, String rightTitle, final OnTitleListener pListener) {
        initTitleView(view, R.drawable.left, title, rightTitle, pListener);
    }

    public static void initTitleView(View view, @DrawableRes int backResId, String title, String rightTitle, final OnTitleListener pListener) {
        ImageView ivBack = (ImageView) view.findViewById(R.id.layout_title_ivBack);
        TextView tvTitle = (TextView) view.findViewById(R.id.layout_title_tvTitle);
        TextView tvRightTitle = (TextView) view.findViewById(R.id.layout_title_tvRightTitle);
        ivBack.setVisibility(pListener == null ? View.GONE : View.VISIBLE);
        ivBack.setImageResource(backResId);
        tvTitle.setText(title);
        tvRightTitle.setText(rightTitle);
        ivBack.setOnClickListener(new Utils.OnClickListener() {
            @Override
            public void onClick2(View v) {
                if (pListener != null) {
                    pListener.onBack();
                }
            }
        });
        tvTitle.setOnClickListener(new Utils.OnClickListener() {
            @Override
            public void onClick2(View v) {
                if (pListener != null) {
                    pListener.onTitle();
                }
            }
        });
        tvRightTitle.setOnClickListener(new Utils.OnClickListener() {
            @Override
            public void onClick2(View v) {
                if (pListener != null) {
                    pListener.onRightTitle();
                }
            }
        });
    }

}
