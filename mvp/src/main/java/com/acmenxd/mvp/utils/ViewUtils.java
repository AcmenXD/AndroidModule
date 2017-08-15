package com.acmenxd.mvp.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.acmenxd.frame.utils.Utils;
import com.acmenxd.mvp.R;
import com.acmenxd.recyclerview.SSwipeRefreshLayout;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/15 15:10
 * @detail view工具类
 */
public final class ViewUtils {
    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnRefreshLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * 设置SSwipeRefreshLayout样式 -> White
     */
    public static void setSSwipeRefreshLayoutStyle$White(Context context, SSwipeRefreshLayout srl, final OnRefreshListener refreshListener) {
        setSSwipeRefreshLayoutStyle(context, srl, 0xff008de7, 0xffffffff, R.drawable.progress_indeterminate_white, R.drawable.down_arrow_white, refreshListener, null);
    }

    /**
     * 设置SSwipeRefreshLayout样式 -> White
     */
    public static void setSSwipeRefreshLayoutStyle$White(Context context, SSwipeRefreshLayout srl, final OnRefreshLoadMoreListener refreshLoadMoreListener) {
        setSSwipeRefreshLayoutStyle(context, srl, 0xff008de7, 0xffffffff, R.drawable.progress_indeterminate_white, R.drawable.down_arrow_white, null, refreshLoadMoreListener);
    }

    /**
     * 设置SSwipeRefreshLayout样式
     */
    public static void setSSwipeRefreshLayoutStyle(Context context, SSwipeRefreshLayout srl, final OnRefreshListener refreshListener) {
        setSSwipeRefreshLayoutStyle(context, srl, 0xfff2f2f2, 0xff000000, R.drawable.progress_indeterminate_black, R.drawable.down_arrow_black, refreshListener, null);
    }

    /**
     * 设置SSwipeRefreshLayout样式
     */
    public static void setSSwipeRefreshLayoutStyle(Context context, SSwipeRefreshLayout srl, final OnRefreshLoadMoreListener refreshLoadMoreListener) {
        setSSwipeRefreshLayoutStyle(context, srl, 0xfff2f2f2, 0xff000000, R.drawable.progress_indeterminate_black, R.drawable.down_arrow_black, null, refreshLoadMoreListener);
    }

    /**
     * 设置SSwipeRefreshLayout样式
     */
    private static void setSSwipeRefreshLayoutStyle(Context context, SSwipeRefreshLayout srl, int bgColor, int textColor, int progressResId, int arrowResId, final OnRefreshListener refreshListener, final OnRefreshLoadMoreListener refreshLoadMoreListener) {
        srl.setHeaderBackgroundColor(bgColor);
        srl.setTargetScrollWithLayout(true);
        if (refreshListener != null || refreshLoadMoreListener != null) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_sswiperefresh, null);
            final TextView tvContent = (TextView) view.findViewById(R.id.layout_header_tvContent);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.layout_header_ivLoading);
            final ImageView ivArrow = (ImageView) view.findViewById(R.id.layout_header_ivArrow);
            tvContent.setTextColor(textColor);
            ivArrow.setImageResource(arrowResId);
            progressBar.setIndeterminateDrawable(context.getResources().getDrawable(progressResId));
            srl.setHeaderView(view);
            srl.setOnRefreshListener(new SSwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    tvContent.setText("正在刷新..");
                    progressBar.setVisibility(View.VISIBLE);
                    ivArrow.setVisibility(View.GONE);
                    if (refreshListener != null) {
                        refreshListener.onRefresh();
                    } else if (refreshLoadMoreListener != null) {
                        refreshLoadMoreListener.onRefresh();
                    }
                }

                @Override
                public void onReach(boolean enable) {
                    tvContent.setText(enable ? "松开立即刷新" : "下拉可以刷新");
                    progressBar.setVisibility(View.GONE);
                    ivArrow.setVisibility(View.VISIBLE);
                    ivArrow.setRotation(enable ? 180 : 0);
                }

                @Override
                public void onPullDistance(int distance) {
                }
            });
        }
        if (refreshLoadMoreListener != null) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_sswiperefresh, null);
            final TextView tvContent = (TextView) view.findViewById(R.id.layout_header_tvContent);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.layout_header_ivLoading);
            final ImageView ivArrow = (ImageView) view.findViewById(R.id.layout_header_ivArrow);
            tvContent.setTextColor(textColor);
            ivArrow.setImageResource(arrowResId);
            progressBar.setIndeterminateDrawable(context.getResources().getDrawable(progressResId));
            srl.setFooterView(view);
            srl.setOnLoadMoreListener(new SSwipeRefreshLayout.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    tvContent.setText("正在加载..");
                    progressBar.setVisibility(View.VISIBLE);
                    ivArrow.setVisibility(View.GONE);
                    refreshLoadMoreListener.onLoadMore();
                }

                @Override
                public void onReach(boolean enable) {
                    tvContent.setText(enable ? "松开立即加载" : "上拉可以加载");
                    progressBar.setVisibility(View.GONE);
                    ivArrow.setVisibility(View.VISIBLE);
                    ivArrow.setRotation(enable ? 0 : 180);
                }

                @Override
                public void onPushDistance(int distance) {
                }
            });
        }
    }

    /**
     * 设置SwipeRefreshLayout样式
     */
    public static void setSwipeRefreshLayoutStyle(Context context, SwipeRefreshLayout srl, SwipeRefreshLayout.OnRefreshListener pOnRefreshListener) {
        srl.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        srl.setProgressBackgroundColorSchemeColor(Color.WHITE);
        srl.setSize(SwipeRefreshLayout.DEFAULT);//SwipeRefreshLayout.LARGE
        srl.setProgressViewEndTarget(true, (int) Utils.dp2px(context, 70));
        srl.setOnRefreshListener(pOnRefreshListener);
    }

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
