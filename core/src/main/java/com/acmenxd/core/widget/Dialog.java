package com.acmenxd.core.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.widget.OrientationHelper;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.frame.utils.DeviceUtils;
import com.acmenxd.frame.utils.StringUtils;
import com.acmenxd.frame.utils.Utils;
import com.acmenxd.core.R;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/19 11:33
 * @detail 弹框窗口
 */
public class Dialog extends android.app.Dialog {
    public interface OnClickListener {
        void onClick(Dialog dialog, View v);
    }

    public static final int BTN_COLOR_TYPE_BLUE = Color.parseColor("#2159E7");
    public static final int BTN_COLOR_TYPE_BLACK = Color.parseColor("#333333");
    public static final int BTN_COLOR_TYPE_GREY = Color.parseColor("#9b9b9b");

    public static class Builder {
        private Context mContext;
        private int mThemeResId;
        private int mWidth; //默认290dip
        private int mHeight; //默认185dip
        private boolean isChangeWH = false;
        private boolean isCancelable = false; //是否可以通过点击Back键取消,默认false
        private boolean isCanceledOnTouchOutside = false; //是否在点击Dialog外部时取消Dialog,默认false
        private boolean isCloseVisible = true; //右上角叉是否显示
        private OnClickListener mCloseListener; //点击右上角叉时回调
        private CharSequence mTitle;
        private int mTitleResId;
        private CharSequence mMessage;
        private SpannableString mSpannableMessage;
        private int mMessageResId;
        private View mCustomView;
        private int mCustomResId;
        private int mBtnOrientation;
        private boolean mBtn1Bool;
        private int mBtn1Color;
        private CharSequence mBtn1Str;
        private OnClickListener mBtn1Listener;
        private boolean mBtn2Bool;
        private int mBtn2Color;
        private CharSequence mBtn2Str;
        private OnClickListener mBtn2Listener;
        private boolean mBtn3Bool;
        private int mBtn3Color;
        private CharSequence mBtn3Str;
        private OnClickListener mBtn3Listener;

        public Builder(@NonNull Context pContext) {
            this(pContext, 0);
        }

        public Builder(@NonNull Context pContext, @StyleRes int pThemeResId) {
            this.mContext = pContext;
            this.mThemeResId = pThemeResId;
            this.mWidth = (int) Utils.dp2px(mContext, 290);
            this.mHeight = (int) Utils.dp2px(mContext, 185);
            this.mBtnOrientation = OrientationHelper.HORIZONTAL;
        }

        /**
         * @param pWidth  dip值
         * @param pHeight dip值
         */
        public Builder setWidthHeight(@IntRange(from = 0) int pWidth, @IntRange(from = 0) int pHeight) {
            this.mWidth = (int) Utils.dp2px(mContext, pWidth);
            this.mHeight = (int) Utils.dp2px(mContext, pHeight);
            isChangeWH = true;
            return this;
        }

        /**
         * 设置固定弹框
         */
        public Builder auto335$230() {
            this.mWidth = (int) Utils.dp2px(mContext, 335);
            this.mHeight = (int) Utils.dp2px(mContext, 230);
            isChangeWH = true;
            return this;
        }

        /**
         * 设置固定弹框
         */
        public Builder auto320$260() {
            this.mWidth = (int) Utils.dp2px(mContext, 320);
            this.mHeight = (int) Utils.dp2px(mContext, 260);
            isChangeWH = true;
            return this;
        }

        /**
         * 设置固定弹框
         */
        public Builder auto335$282() {
            this.mWidth = (int) Utils.dp2px(mContext, 335);
            this.mHeight = (int) Utils.dp2px(mContext, 282);
            isChangeWH = true;
            return this;
        }

        /**
         * 是否可以通过点击Back键取消,默认false
         */
        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 是否在点击Dialog外部时取消Dialog,默认false
         */
        public Builder setCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
            this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
            return this;
        }

        /**
         * 右上角叉是否显示,默认显示
         */
        public Builder setCloseVisible(boolean pIsCloseVisible) {
            this.isCloseVisible = pIsCloseVisible;
            return this;
        }

        /**
         * 点击右上角叉时回调
         */
        public Builder setCloseListener(@Nullable OnClickListener pCloseListener) {
            this.mCloseListener = pCloseListener;
            return this;
        }

        public Builder setTitle(@NonNull CharSequence pTitle) {
            this.mTitle = pTitle;
            return this;
        }

        public Builder setTitle(@StringRes int pTitleResId) {
            this.mTitleResId = pTitleResId;
            return this;
        }

        public Builder setMessage(@NonNull CharSequence pMessage) {
            this.mMessage = pMessage;
            return this;
        }

        public Builder setMessage(@NonNull SpannableString pMessage) {
            this.mSpannableMessage = pMessage;
            return this;
        }

        public Builder setMessage(@StringRes int pMessageResId) {
            this.mMessageResId = pMessageResId;
            return this;
        }

        public Builder setCustomView(@NonNull View pCustomView) {
            this.mCustomView = pCustomView;
            return this;
        }

        public Builder setCustomView(@LayoutRes int pCustomResId) {
            this.mCustomResId = pCustomResId;
            return this;
        }

        public Builder setBtnOrientation(@IntRange(from = OrientationHelper.HORIZONTAL, to = OrientationHelper.VERTICAL) int pBtnOrientation) {
            this.mBtnOrientation = pBtnOrientation;
            return this;
        }

        /**
         * @param pBtn1Str      为null则默认"确认"
         * @param pBtn1Listener 为null则默认事件,单击关闭Dialog
         */
        public Builder setButton1(@Nullable CharSequence pBtn1Str, @Nullable OnClickListener pBtn1Listener) {
            this.mBtn1Bool = true;
            this.mBtn1Str = pBtn1Str;
            this.mBtn1Color = BTN_COLOR_TYPE_BLUE;
            this.mBtn1Listener = pBtn1Listener;
            return this;
        }

        /**
         * @param pBtn1Color 默认为蓝色
         */
        public Builder setButton1(@Nullable CharSequence pBtn1Str, int pBtn1Color, @Nullable OnClickListener pBtn1Listener) {
            setButton1(pBtn1Str, pBtn1Listener);
            this.mBtn1Color = pBtn1Color;
            return this;
        }

        /**
         * @param pBtn2Str      为null则默认"忽略"
         * @param pBtn2Listener 为null则默认事件,单击关闭Dialog
         */
        public Builder setButton2(@Nullable CharSequence pBtn2Str, @Nullable OnClickListener pBtn2Listener) {
            this.mBtn2Bool = true;
            this.mBtn2Str = pBtn2Str;
            this.mBtn2Color = BTN_COLOR_TYPE_BLACK;
            this.mBtn2Listener = pBtn2Listener;
            return this;
        }

        /**
         * @param pBtn2Color 默认为黑色
         */
        public Builder setButton2(@Nullable CharSequence pBtn2Str, int pBtn2Color, @Nullable OnClickListener pBtn2Listener) {
            setButton2(pBtn2Str, pBtn2Listener);
            this.mBtn2Color = pBtn2Color;
            return this;
        }

        /**
         * @param pBtn3Str      为null则默认"忽略"
         * @param pBtn3Listener 为null则默认事件,单击关闭Dialog
         */
        public Builder setButton3(@Nullable CharSequence pBtn3Str, @Nullable OnClickListener pBtn3Listener) {
            this.mBtn3Bool = true;
            this.mBtn3Str = pBtn3Str;
            this.mBtn3Color = BTN_COLOR_TYPE_BLACK;
            this.mBtn3Listener = pBtn3Listener;
            return this;
        }

        /**
         * @param pBtn3Color 默认为黑色
         */
        public Builder setButton3(@Nullable CharSequence pBtn3Str, int pBtn3Color, @Nullable OnClickListener pBtn3Listener) {
            setButton3(pBtn3Str, pBtn3Listener);
            this.mBtn3Color = pBtn3Color;
            return this;
        }

        public Dialog build() {
            // 配置Dialog
            final Dialog dialog = new Dialog(mContext);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.widget_dialog);
            dialog.setCancelable(isCancelable);
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
            // 去掉蓝线
            int divierId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = dialog.findViewById(divierId);
            if (divider != null) {
                divider.setBackgroundColor(Color.TRANSPARENT);
            }
            // 初始化控件
            LinearLayout dialogLayout = (LinearLayout) dialog.findViewById(R.id.dialog_layout);
            LinearLayout closeLayout = (LinearLayout) dialog.findViewById(R.id.dialog_closeLayout);
            LinearLayout contentLayout = (LinearLayout) dialog.findViewById(R.id.dialog_layout_content);
            LinearLayout btnLayout = (LinearLayout) dialog.findViewById(R.id.dialog_layout_btn);
            ImageView ibClose = (ImageView) dialog.findViewById(R.id.dialog_close);
            TextView tvTitle = (TextView) dialog.findViewById(R.id.dialog_tv_title);
            TextView tvMessage = (TextView) dialog.findViewById(R.id.dialog_tv_message);
            Button btn1 = (Button) dialog.findViewById(R.id.dialog_btn1);
            Button btn2 = (Button) dialog.findViewById(R.id.dialog_btn2);
            Button btn3 = (Button) dialog.findViewById(R.id.dialog_btn3);
            View btn1Line = dialog.findViewById(R.id.dialog_btnLine1);
            View btn2Line = dialog.findViewById(R.id.dialog_btnLine2);
            View btn3Line = dialog.findViewById(R.id.dialog_btnLine3);
            // 填充视图
            CharSequence title = mTitleResId != 0 ? mContext.getString(mTitleResId) : mTitle;
            View customView = mCustomResId != 0 ? LayoutInflater.from(mContext).inflate(mCustomResId, contentLayout, false) : mCustomView;
            if (StringUtils.isEmpty(title) && !isChangeWH && customView == null) {
                mHeight = (int) Utils.dp2px(mContext, 150);
                tvMessage.setPadding(0, 0, 0, 0);
            }
            if (mWidth >= DeviceUtils.getScreenX(mContext)) {
                mWidth = DeviceUtils.getScreenX(mContext) - (int) Utils.dp2px(mContext, 20);
            }
            dialogLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHeight));
            closeLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, FrameLayout.LayoutParams.WRAP_CONTENT));
            if (isCloseVisible) {
                ibClose.setVisibility(View.VISIBLE);
            } else {
                ibClose.setVisibility(View.GONE);
            }
            if (mCloseListener == null) {
                ibClose.setOnClickListener(new Utils.OnClickListener() {
                    @Override
                    public void onClick2(View v) {
                        dialog.dismiss();
                    }
                });
            } else {
                ibClose.setOnClickListener(new Utils.OnClickListener() {
                    @Override
                    public void onClick2(View v) {
                        mCloseListener.onClick(dialog, v);
                    }
                });
            }
            if (StringUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            }
            if (customView != null) {
                tvMessage.setVisibility(View.GONE);
                contentLayout.addView(customView);
            } else {
                CharSequence message = mMessageResId != 0 ? mContext.getString(mMessageResId) : mMessage;
                if (StringUtils.isEmpty(mSpannableMessage)) {
                    if (StringUtils.isEmpty(message)) {
                        tvMessage.setVisibility(View.GONE);
                    } else {
                        tvMessage.setText(message);
                        tvMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvMessage.setText(mSpannableMessage);
                    tvMessage.setVisibility(View.VISIBLE);
                }
            }
            int btnNumber = 0;
            int btnWidth = LinearLayout.LayoutParams.MATCH_PARENT;
            btnNumber = mBtn1Bool ? btnNumber + 1 : btnNumber;
            btnNumber = mBtn2Bool ? btnNumber + 1 : btnNumber;
            btnNumber = mBtn3Bool ? btnNumber + 1 : btnNumber;
            btnLayout.setOrientation(mBtnOrientation);
            if (this.mBtnOrientation == OrientationHelper.HORIZONTAL && btnNumber > 0) {
                btnWidth = mWidth / btnNumber;
            }
            btn1Line.setVisibility(View.GONE);
            btn2Line.setVisibility(View.GONE);
            btn3Line.setVisibility(View.GONE);
            if (mBtn1Bool) {
                btn1Line.setVisibility(View.VISIBLE);
                btn1.setLayoutParams(new LinearLayout.LayoutParams(btnWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!StringUtils.isEmpty(mBtn1Str)) {
                    btn1.setText(mBtn1Str);
                }
                if (mBtn1Listener != null) {
                    btn1.setOnClickListener(new Utils.OnClickListener() {
                        @Override
                        public void onClick2(View v) {
                            mBtn1Listener.onClick(dialog, v);
                        }
                    });
                } else {
                    btn1.setOnClickListener(new Utils.OnClickListener() {
                        @Override
                        public void onClick2(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                btn1.setTextColor(mBtn1Color);
                btn1.setVisibility(View.VISIBLE);
            } else {
                btn1.setVisibility(View.GONE);
            }
            if (mBtn2Bool) {
                btn2Line.setVisibility(View.VISIBLE);
                btn2.setLayoutParams(new LinearLayout.LayoutParams(btnWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!StringUtils.isEmpty(mBtn2Str)) {
                    btn2.setText(mBtn2Str);
                }
                if (mBtn2Listener != null) {
                    btn2.setOnClickListener(new Utils.OnClickListener() {
                        @Override
                        public void onClick2(View v) {
                            mBtn2Listener.onClick(dialog, v);
                        }
                    });
                } else {
                    btn2.setOnClickListener(new Utils.OnClickListener() {
                        @Override
                        public void onClick2(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                btn2.setTextColor(mBtn2Color);
                btn2.setVisibility(View.VISIBLE);
            } else {
                btn2.setVisibility(View.GONE);
            }
            if (mBtn3Bool) {
                btn3Line.setVisibility(View.VISIBLE);
                btn3.setLayoutParams(new LinearLayout.LayoutParams(btnWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!StringUtils.isEmpty(mBtn3Str)) {
                    btn3.setText(mBtn3Str);
                }
                if (mBtn3Listener != null) {
                    btn3.setOnClickListener(new Utils.OnClickListener() {
                        @Override
                        public void onClick2(View v) {
                            mBtn3Listener.onClick(dialog, v);
                        }
                    });
                } else {
                    btn3.setOnClickListener(new Utils.OnClickListener() {
                        @Override
                        public void onClick2(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                btn3.setTextColor(mBtn3Color);
                btn3.setVisibility(View.VISIBLE);
            } else {
                btn3.setVisibility(View.GONE);
            }
            return dialog;
        }
    }

    public Dialog(@NonNull Context pContext) {
        this(pContext, 0);
    }

    public Dialog(@NonNull Context pContext, @StyleRes int pThemeResId) {
        super(pContext, pThemeResId);
    }

    /**
     * 右上角叉是否显示,默认显示
     */
    public void setCloseVisible(boolean pIsCloseVisible) {
        ImageView ibClose = (ImageView) findViewById(R.id.dialog_close);
        if (pIsCloseVisible) {
            ibClose.setVisibility(View.VISIBLE);
        } else {
            ibClose.setVisibility(View.GONE);
        }
    }

    /**
     * 点击右上角叉时回调
     */
    public void setCloseListener(@Nullable Utils.OnClickListener pCloseListener) {
        ImageView ibClose = (ImageView) findViewById(R.id.dialog_close);
        if (pCloseListener == null) {
            ibClose.setOnClickListener(new Utils.OnClickListener() {
                @Override
                public void onClick2(View v) {
                    Dialog.this.dismiss();
                }
            });
        } else {
            ibClose.setOnClickListener(pCloseListener);
        }
    }

}
