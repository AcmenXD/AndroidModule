package com.acmenxd.mvp.widget.frame;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.mvp.utils.Utils;
import com.acmenxd.mvp.widget.CircleProgress;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/13 17:54
 * @detail 基础视图类
 */
public final class BasisView {

    /**
     * 获取加载时显示的视图
     */
    public static View getLoadingView(Context pContext) {
        LinearLayout loadLayout = new LinearLayout(pContext);
        loadLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadLayout.setOrientation(LinearLayout.VERTICAL);
        loadLayout.setGravity(Gravity.CENTER);
        // 进度progressBar
        ImageView ivProgress = new ImageView(pContext);
        CircleProgress progress = new CircleProgress(pContext, ivProgress);
        progress.updateSizes(CircleProgress.LARGE);
        ivProgress.setImageDrawable(progress);
        loadLayout.addView(ivProgress);
        // 文本
        TextView loadTV = new TextView(pContext);
        loadTV.setGravity(Gravity.CENTER);
        loadTV.setTextSize(18);
        loadTV.setTextColor(Color.GRAY);
        loadTV.setText("正在加载...");
        loadTV.setPadding(0, (int) Utils.dp2px(pContext, 4), 0, 0);
        loadLayout.addView(loadTV);
        return loadLayout;
    }

    /**
     * 获取出错时显示的视图
     */
    public static View getErrorView(Context pContext) {
        LinearLayout errorLayout = new LinearLayout(pContext);
        errorLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorLayout.setOrientation(LinearLayout.VERTICAL);
        errorLayout.setGravity(Gravity.CENTER);
        // 文本
        TextView loadTV = new TextView(pContext);
        loadTV.setGravity(Gravity.CENTER);
        loadTV.setTextSize(18);
        loadTV.setTextColor(Color.GRAY);
        loadTV.setText("检查网络");
        loadTV.setPadding(0, (int) Utils.dp2px(pContext, 4), 0, 0);
        errorLayout.addView(loadTV);
        return errorLayout;
    }

    /**
     * 获取Dialog弹框
     */
    public static View getDialogView(Context pContext) {
        int width = (int) Utils.dp2px(pContext, 130);
        int height = (int) Utils.dp2px(pContext, 90);
        LinearLayout loadLayout = new LinearLayout(pContext);
        loadLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 内容填充Layout
        LinearLayout viewLayout = new LinearLayout(pContext);
        viewLayout.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        viewLayout.setOrientation(LinearLayout.VERTICAL);
        viewLayout.setGravity(Gravity.CENTER);
        loadLayout.addView(viewLayout);
        // 进度progressBar
        ImageView ivProgress = new ImageView(pContext);
        //ProgressBar progressBar = new ProgressBar(pContext, null, android.R.attr.progressBarStyle);
        CircleProgress progressBar = new CircleProgress(pContext, ivProgress);
        progressBar.updateSizes(CircleProgress.LARGE);
        ivProgress.setImageDrawable(progressBar);
        viewLayout.addView(ivProgress);
        // 文本
        TextView loadTV = new TextView(pContext);
        loadTV.setGravity(Gravity.CENTER);
        loadTV.setTextSize(16);
        loadTV.setTextColor(Color.BLACK);
        loadTV.setText("正在加载...");
        loadTV.setPadding(0, (int) Utils.dp2px(pContext, 4), 0, 0);
        viewLayout.addView(loadTV);
        return loadLayout;
    }

    /**
     * 获取进入动画
     */
    public static Animation getInAnimation(Context pContext) {
        return AnimationUtils.makeInAnimation(pContext, true);
    }

    /**
     * 获取退出动画
     */
    public static Animation getOutAnimation(Context pContext) {
        return AnimationUtils.makeOutAnimation(pContext, true);
    }

    /**
     * 设置Layouts的显隐状态
     */
    public static void layouts$setVisibility(final View pInView, final View... pViews) {
        for (int i = 0, len = pViews.length; i < len; i++) {
            if (pViews[i] == pInView) {
                pViews[i].setVisibility(View.VISIBLE);
            } else {
                pViews[i].setVisibility(View.GONE);
            }
        }
    }

    /**
     * 关闭掉所有动画后,执行in&out动画
     */
    public static void layoutCancelInOutAnimation(final Context pContext, final View pInView, final View... pViews) {
        boolean hasNoEnd = false; // 是否有动画没有执行完
        if (pViews != null && pViews.length > 0) {
            for (int i = 0, len = pViews.length; i < len; i++) {
                Animation oldAnimation = pViews[i].getAnimation();
                if (oldAnimation != null) {
                    if (!oldAnimation.hasEnded()) {
                        hasNoEnd = true;
                    }
                    oldAnimation.cancel();
                    pViews[i].clearAnimation();
                }
            }
            if (hasNoEnd) {
                pInView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pInView.getVisibility() == View.VISIBLE) {
                            layouts$setVisibility(pInView, pViews);
                        } else {
                            layoutStartInOutAnimation(pContext, pInView, pViews);
                        }
                    }
                }, 10);
            } else {
                if (pInView.getVisibility() == View.VISIBLE) {
                    layouts$setVisibility(pInView, pViews);
                } else {
                    layoutStartInOutAnimation(pContext, pInView, pViews);
                }
            }
        }
    }

    /**
     * 设置pInView执行in动画,其他视图全部执行out动画
     */
    public static void layoutStartInOutAnimation(final Context pContext, final View pInView, final View... pViews) {
        for (int i = 0, len = pViews.length; i < len; i++) {
            if (pViews[i] != pInView && pViews[i].getVisibility() == View.VISIBLE) {
                pViews[i].startAnimation(BasisView.getOutAnimation(pContext));
            }
        }
        Animation animation = BasisView.getInAnimation(pContext);
        pInView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation pAnimation) {
                pInView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation pAnimation) {
                layouts$setVisibility(pInView, pViews);
            }

            @Override
            public void onAnimationRepeat(Animation pAnimation) {

            }
        });
    }

}
