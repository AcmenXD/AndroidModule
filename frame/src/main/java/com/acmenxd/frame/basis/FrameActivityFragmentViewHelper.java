package com.acmenxd.frame.basis;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.frame.utils.Utils;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/13 17:54
 * @detail FrameActivity & FrameFragment视图帮助类
 */
public final class FrameActivityFragmentViewHelper {

    /**
     * 获取加载时显示的视图
     */
    public static View getLoadingView(@NonNull Context pContext) {
        LinearLayout loadLayout = new LinearLayout(pContext);
        loadLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loadLayout.setOrientation(LinearLayout.VERTICAL);
        loadLayout.setGravity(Gravity.CENTER);
        // 文本
        int pading = (int) Utils.dp2px(pContext, 10);
        TextView loadTV = new TextView(pContext);
        loadTV.setGravity(Gravity.CENTER);
        loadTV.setTextSize(16);
        loadTV.setTextColor(Color.BLACK);
        loadTV.setText("正在加载...");
        loadTV.setPadding(pading, pading, pading, pading);
        loadLayout.addView(loadTV);
        return loadLayout;
    }

    /**
     * 获取出错时显示的视图
     */
    public static View getErrorView(@NonNull Context pContext) {
        LinearLayout errorLayout = new LinearLayout(pContext);
        errorLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorLayout.setOrientation(LinearLayout.VERTICAL);
        errorLayout.setGravity(Gravity.CENTER);
        // 文本
        TextView errorTV = new TextView(pContext);
        errorTV.setGravity(Gravity.CENTER);
        errorTV.setTextSize(16);
        errorTV.setTextColor(Color.BLACK);
        errorTV.setText("请求失败，请检查网络！");
        errorTV.setPadding(0, (int) Utils.dp2px(pContext, 10), 0, 0);
        errorLayout.addView(errorTV);
        return errorLayout;
    }

    /**
     * 获取LoadingDialog弹框
     */
    public static View getLoadingDialogView(@NonNull Context pContext) {
        LinearLayout loadLayout = new LinearLayout(pContext);
        loadLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loadLayout.setOrientation(LinearLayout.VERTICAL);
        loadLayout.setGravity(Gravity.CENTER);
        loadLayout.setBackgroundColor(Color.BLACK);
        // 文本
        int pading = (int) Utils.dp2px(pContext, 10);
        TextView loadTV = new TextView(pContext);
        loadTV.setGravity(Gravity.CENTER);
        loadTV.setTextSize(16);
        loadTV.setTextColor(Color.WHITE);
        loadTV.setText("正在加载...");
        loadTV.setPadding(pading, pading, pading, pading);
        loadLayout.addView(loadTV);
        return loadLayout;
    }

    /**
     * 获取进入动画 -> ContentView - LoadingView - ErrorView 切换时
     */
    public static Animation getInAnimation(@NonNull Context pContext) {
        return null;
        // return AnimationUtils.makeInAnimation(pContext, true);
    }

    /**
     * 获取退出动画 -> ContentView - LoadingView - ErrorView 切换时
     */
    public static Animation getOutAnimation(@NonNull Context pContext) {
        return null;
        // return AnimationUtils.makeOutAnimation(pContext, true);
    }

    /**
     * 设置Layouts的显隐状态
     */
    public static void layouts$setVisibility(@NonNull final View pInView, @NonNull final View... pViews) {
        for (int i = 0, len = pViews.length; i < len; i++) {
            if (pViews[i] == pInView) {
                pViews[i].setVisibility(View.VISIBLE);
            } else {
                pViews[i].setVisibility(View.GONE);
            }
        }
    }

    /**
     * 关闭掉所有动画后,重新执行in&out动画
     */
    public static void layoutRestartInOutAnimation(@NonNull final Context pContext, @NonNull final View pInView, @NonNull final View... pViews) {
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
    private static void layoutStartInOutAnimation(@NonNull final Context pContext, @NonNull final View pInView, @NonNull final View... pViews) {
        for (int i = 0, len = pViews.length; i < len; i++) {
            if (pViews[i] != pInView && pViews[i].getVisibility() == View.VISIBLE) {
                Animation outAnimation = FrameActivityFragmentViewHelper.getOutAnimation(pContext);
                if (outAnimation != null) {
                    pViews[i].startAnimation(outAnimation);
                }
            }
        }
        Animation animation = FrameActivityFragmentViewHelper.getInAnimation(pContext);
        if (animation == null) {
            pInView.setVisibility(View.VISIBLE);
            layouts$setVisibility(pInView, pViews);
        } else {
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

}
