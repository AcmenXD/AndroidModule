package com.acmenxd.frame.widget.banner;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * @detail 轮播组件, 用于嵌套在RecyclerView中
 */
public class RecyclerBannerView extends RelativeLayout implements View.OnTouchListener {
    public interface OnListener<T> {
        void onClick(int position, T pData);

        ViewGroup getItemView(int position, T pData);

        int getIndicatorResource();
    }

    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLayout;
    private ImageView mIvDefault;
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

    private int time;
    private boolean isDown;
    private long lastDownTime;

    public RecyclerBannerView(Context context) {
        this(context, null);
    }

    public RecyclerBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        autoDuration = 5;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (autoDuration > 0 && mAdapter != null && mAdapter.getCount() > 1) {
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
        }, 1000, 1000);

        mIvDefault = new ImageView(mContext);
        mIvDefault.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mIvDefault.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(mIvDefault);
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
    public void setOnListener(OnListener pItemClickListener) {
        this.mOnListener = pItemClickListener;
    }

    /**
     * 设置自动播放时间(秒),默认3秒,设置0为不自动播放
     */
    public void setAutoDuration(int pAutoDuration) {
        autoDuration = pAutoDuration;
    }

    /**
     * 设置指示器 圆点位置 默认:Gravity.CENTER居中
     */
    public void setIndicatorGravity(int pIndicatorGravity) {
        indicatorGravity = pIndicatorGravity;
    }

    /**
     * 设置指示器 圆点直径(dip值) 默认:defaultSize->6
     */
    public void setIndicatorDiameterDip(int pIndicatorDiameterDip) {
        indicatorDiameterDip = (int) Utils.dp2px(mContext, pIndicatorDiameterDip);
    }

    /**
     * 设置指示器 圆点间距(dip值) 默认:defaultSize->6
     */
    public void setIndicatorSpaceDip(int pIndicatorSpaceDip) {
        indicatorSpaceDip = (int) Utils.dp2px(mContext, pIndicatorSpaceDip);
    }

    /**
     * 设置默认视图以及Listener
     */
    public void setDefaultView(@DrawableRes int pResId, OnClickListener pListener) {
        mIvDefault.setImageResource(pResId);
        mIvDefault.setOnClickListener(pListener);
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
    public void setDataCommit(List<?> pDatas) {
        if (pDatas == null || pDatas.size() <= 0) {
            mIvDefault.setVisibility(VISIBLE);
            mIndicatorLayout.setVisibility(GONE);
            mViewPager.setVisibility(GONE);
            return;
        }
        mIvDefault.setVisibility(GONE);
        mIndicatorLayout.setVisibility(VISIBLE);
        mViewPager.setVisibility(VISIBLE);
        // 重置参数
        time = 0;
        isDown = false;
        lastDownTime = 0;
        mViewPager.clearOnPageChangeListeners();

        this.datas = pDatas;
        this.indicatorViews = new ArrayList<>();
        mIndicatorLayout.removeAllViews();
        mIndicatorLayout.setGravity(indicatorGravity);
        mIndicatorLayout.setPadding(0, 0, 0, indicatorDiameterDip * 2);
        if (datas.size() <= 1) {
            mIndicatorLayout.setVisibility(GONE);
        } else {
            mIndicatorLayout.setVisibility(VISIBLE);
        }
        for (int i = 0, len = datas.size(); i < len; i++) {
            int indicatorResource = mOnListener.getIndicatorResource();
            View view = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorDiameterDip, indicatorDiameterDip);
            params.leftMargin = indicatorSpaceDip;
            view.setLayoutParams(params);
            if (indicatorResource > 0) {
                view.setBackgroundResource(mOnListener.getIndicatorResource());
            }
            if (len == 2) {
                mIndicatorLayout.addView(view, 0);
            } else {
                mIndicatorLayout.addView(view);
            }
            indicatorViews.add(view);
        }
        if (mAdapter != null) {
            mViewPager.setCurrentItem(currPosition, false);
        }
        mAdapter = new BannerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int tempPosition = position % datas.size();
                if (tempPosition != currPosition) {
                    time = 0;
                }
                currPosition = tempPosition;
                for (int i = 0, len = indicatorViews.size(); i < len; i++) {
                    if (i == currPosition) {
                        indicatorViews.get(i).setSelected(true);
                    } else {
                        indicatorViews.get(i).setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (currPosition >= datas.size()) {
            currPosition = datas.size() - 1;
        }
        mViewPager.setCurrentItem(datas.size() + currPosition);
        indicatorViews.get(currPosition).setSelected(true);
    }

    class BannerAdapter extends PagerAdapter {
        private int realCount;
        private int count;

        public BannerAdapter() {
            this.realCount = datas.size();
            if (realCount > 1) {
                count = 10000;
            } else {
                count = realCount;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = position % realCount;
            if(index < datas.size()) {
                ViewGroup item = mOnListener.getItemView(index, datas.get(index));
                item.setOnTouchListener(RecyclerBannerView.this);
                ViewGroup parent = (ViewGroup) item.getParent();
                if (parent != null) {
                    parent.removeView(item);
                }
                container.addView(item);
                return item;
            }
            return null;
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

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            if (realCount > 1) {
                if (mViewPager.getCurrentItem() == 0) {
                    mViewPager.setCurrentItem(realCount, false);
                } else if (mViewPager.getCurrentItem() == count - 1) {
                    mViewPager.setCurrentItem(realCount - 1, false);
                }
            }
        }
    }
}
