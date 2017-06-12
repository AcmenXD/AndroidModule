package com.acmenxd.frame.widget;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.acmenxd.frame.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/9 11:29
 * @detail 轮播组件, 不能嵌套在RecyclerView中
 */
public final class BannerView extends RelativeLayout implements View.OnTouchListener {
    public interface OnListener<T> {
        void onClick(@IntRange(from = 0) int position, @NonNull T pData);

        ViewGroup getItemView(@IntRange(from = 0) int position, @NonNull T pData);

        int getIndicatorResource();
    }

    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLayout;
    private List<View> indicatorViews;
    private List<?> datas;

    private final int defaultSize = 6;
    private BannerAdapter mAdapter;
    private OnListener mOnListener;
    private int indicatorGravity; // 指示器 圆点位置
    private int indicatorDiameterDip; // 指示器 圆点直径
    private int indicatorSpaceDip; // 指示器 圆点间距
    private int autoDuration; // 自动播放时间(秒)
    private int currPosition; // 当前选中项
    private boolean isNeedChange;
    private int needChangePosition;

    private int time;
    private boolean isDragging;
    private boolean isDown;
    private long lastDownTime;
    private int previousPosition = -1;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mViewPager = new ViewPager(mContext);
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.addView(mViewPager);
        mIndicatorLayout = new LinearLayout(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
        mIndicatorLayout.setLayoutParams(params);
        mIndicatorLayout.setOrientation(OrientationHelper.HORIZONTAL);
        this.addView(mIndicatorLayout);

        indicatorDiameterDip = (int) Utils.dp2px(mContext, defaultSize);
        indicatorSpaceDip = (int) Utils.dp2px(mContext, defaultSize);
        indicatorGravity = Gravity.CENTER;
        autoDuration = 3;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (autoDuration > 0 && mAdapter != null && mAdapter.isCirculation) {
                    if (lastDownTime != 0) {
                        return;
                    }
                    if (isDown) {
                        time -= 1;
                        if (time <= 0) {
                            time = 0;
                        }
                        isDown = false;
                    }
                    if (!isDragging) {
                        time += 1;
                        if (time >= autoDuration) {
                            mViewPager.post(new Runnable() {
                                @Override
                                public void run() {
                                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                                }
                            });
                        }
                    }
                }
            }
        }, 1000, 1000);
    }

    /**
     * 获取当前选中项
     */
    public int getCurrPosition() {
        return currPosition;
    }

    /**
     * 获取监听
     */
    public OnListener getOnListener() {
        return mOnListener;
    }

    /**
     * 设置监听
     */
    public void setOnListener(@NonNull OnListener pItemClickListener) {
        this.mOnListener = pItemClickListener;
    }

    /**
     * 设置自动播放时间(秒)设置0为不自动播放
     */
    public void setAutoDuration(@IntRange(from = 0) int pAutoDuration) {
        autoDuration = pAutoDuration;
    }

    /**
     * 设置指示器 圆点位置 默认:Gravity.CENTER
     */
    public void setIndicatorGravity(@IntRange(from = 0) int pIndicatorGravity) {
        indicatorGravity = pIndicatorGravity;
    }

    /**
     * 设置指示器 圆点直径(dip值) 默认:defaultSize->6
     */
    public void setIndicatorDiameterDip(@IntRange(from = 0) int pIndicatorDiameterDip) {
        indicatorDiameterDip = (int) Utils.dp2px(mContext, pIndicatorDiameterDip);
    }

    /**
     * 设置指示器 圆点间距(dip值) 默认:defaultSize->6
     */
    public void setIndicatorSpaceDip(@IntRange(from = 0) int pIndicatorSpaceDip) {
        indicatorSpaceDip = (int) Utils.dp2px(mContext, pIndicatorSpaceDip);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastDownTime = System.currentTimeMillis();
                isDown = true;
                break;
            case MotionEvent.ACTION_UP:
                if (mOnListener != null && datas.size() > 0 && (System.currentTimeMillis() - lastDownTime < 500)) {
                    mOnListener.onClick(currPosition, datas.get(currPosition));
                }
                lastDownTime = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                lastDownTime = 0;
                break;
        }
        return true;
    }

    /**
     * 设置数据
     */
    public void setDataCommit(@NonNull List<?> pDatas) {
        if (pDatas == null || pDatas.size() <= 0) {
            return;
        }
        // 重置参数
        isNeedChange = false;
        needChangePosition = 0;
        time = 0;
        isDragging = false;
        isDown = false;
        lastDownTime = 0;
        previousPosition = -1;
        mViewPager.clearOnPageChangeListeners();

        this.datas = pDatas;
        this.indicatorViews = new ArrayList<>();
        mIndicatorLayout.removeAllViews();
        mIndicatorLayout.setGravity(indicatorGravity);
        mIndicatorLayout.setPadding(0, 0, 0, indicatorDiameterDip * 2);
        for (int i = 0, len = datas.size(); i < len; i++) {
            int indicatorResource = mOnListener.getIndicatorResource();
            View view = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorDiameterDip, indicatorDiameterDip);
            params.leftMargin = indicatorSpaceDip;
            view.setLayoutParams(params);
            if (indicatorResource > 0) {
                view.setBackgroundResource(indicatorResource);
            }
            mIndicatorLayout.addView(view);
            indicatorViews.add(view);
        }
        mAdapter = new BannerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mAdapter.isCirculation) {
                    if (position == 0) {
                        isNeedChange = true;
                        needChangePosition = mAdapter.getCount() - 2;
                    } else if (position == mAdapter.getCount() - 1) {
                        isNeedChange = true;
                        needChangePosition = 1;
                    }
                    currPosition = position - 1;
                    if (currPosition < 0) {
                        currPosition = datas.size() - 1;
                    } else if (currPosition >= datas.size()) {
                        currPosition = 0;
                    }
                } else {
                    isNeedChange = false;
                    currPosition = position;
                }
                for (int i = 0, len = indicatorViews.size(); i < len; i++) {
                    if (i == currPosition) {
                        indicatorViews.get(i).setSelected(true);
                    } else {
                        indicatorViews.get(i).setSelected(false);
                    }
                }
                if (position != previousPosition) {
                    previousPosition = position;
                    time = 0;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    isDragging = true;
                } else {
                    isDragging = false;
                }
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (isNeedChange) {
                        isNeedChange = false;
                        mViewPager.setCurrentItem(needChangePosition, false);
                    }
                }
            }
        });
        if (mAdapter.isCirculation) {
            int position = currPosition + 1;
            if (position > datas.size()) {
                position = datas.size();
            }
            currPosition = position - 1;
            mViewPager.setCurrentItem(position);
            indicatorViews.get(currPosition).setSelected(true);
        } else {
            currPosition = 0;
            mViewPager.setCurrentItem(currPosition);
            indicatorViews.get(currPosition).setSelected(true);
        }
    }

    final class BannerAdapter extends PagerAdapter {
        private boolean isCirculation;
        private int realCount;
        private int count;

        public BannerAdapter() {
            realCount = datas.size();
            if (realCount > 1) {
                count = realCount + 2;
                isCirculation = true;
            } else {
                count = realCount;
                isCirculation = false;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = position;
            if (isCirculation) {
                if (index == 0) {
                    index = realCount - 1;
                } else if (index == count - 1) {
                    index = 0;
                } else {
                    index -= 1;
                }
            }
            ViewGroup item = mOnListener.getItemView(index, datas.get(index));
            item.setOnTouchListener(BannerView.this);
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
