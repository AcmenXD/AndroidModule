package com.acmenxd.frame.widget;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.frame.R;
import com.acmenxd.frame.utils.Utils;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/19 11:33
 * @detail 弹框窗口
 */
public class Dialog extends android.app.Dialog {
    public static class Builder {
        private Context mContext;
        private int mThemeResId;
        private int mWidth; //默认320dip
        private int mHeight; //默认250dip
        private int mPadding; //默认10dip
        private int mContentPadding; //默认10dip
        private boolean isCancelable = false; //是否可以通过点击Back键取消,默认false
        private boolean isCanceledOnTouchOutside = false; //是否在点击Dialog外部时取消Dialog,默认false
        private boolean isCloseVisible = true; //右上角叉是否显示
        private View.OnClickListener mCloseListener; //点击右上角叉时回调
        private CharSequence mTitle;
        private int mTitleResId;
        private CharSequence mMessage;
        private int mMessageResId;
        private View mCustomView;
        private int mCustomResId;
        private int mBtnOrientation;
        private boolean mBtn1Bool;
        private CharSequence mBtn1Str;
        private View.OnClickListener mBtn1Listener;
        private boolean mBtn2Bool;
        private CharSequence mBtn2Str;
        private View.OnClickListener mBtn2Listener;
        private boolean mBtn3Bool;
        private CharSequence mBtn3Str;
        private View.OnClickListener mBtn3Listener;

        public Builder(@NonNull Context pContext) {
            this(pContext, 0);
        }

        public Builder(@NonNull Context pContext, @StyleRes int pThemeResId) {
            this.mContext = pContext;
            this.mThemeResId = pThemeResId;
            this.mWidth = (int) Utils.dp2px(mContext, 320);
            this.mHeight = (int) Utils.dp2px(mContext, 250);
            this.mPadding = (int) Utils.dp2px(mContext, 10);
            this.mContentPadding = (int) Utils.dp2px(mContext, 10);
            this.mBtnOrientation = OrientationHelper.HORIZONTAL;
        }

        /**
         * @param pWidth  dip值
         * @param pHeight dip值
         */
        public Builder setWidthHeight(@IntRange(from = 0) int pWidth, @IntRange(from = 0) int pHeight) {
            this.mWidth = (int) Utils.dp2px(mContext, pWidth);
            this.mHeight = (int) Utils.dp2px(mContext, pHeight);
            return this;
        }

        /**
         * @param pPadding dip值
         */
        public Builder setPadding(@IntRange(from = 0) int pPadding) {
            this.mPadding = pPadding;
            return this;
        }

        /**
         * @param pContentPadding dip值
         */
        public Builder setContentPadding(@IntRange(from = 0) int pContentPadding) {
            this.mContentPadding = pContentPadding;
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
        public Builder setCloseListener(@Nullable View.OnClickListener pCloseListener) {
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
        public Builder setButton1(@Nullable CharSequence pBtn1Str, @Nullable View.OnClickListener pBtn1Listener) {
            this.mBtn1Bool = true;
            this.mBtn1Str = pBtn1Str;
            this.mBtn1Listener = pBtn1Listener;
            return this;
        }

        /**
         * @param pBtn2Str      为null则默认"取消"
         * @param pBtn2Listener 为null则默认事件,单击关闭Dialog
         */
        public Builder setButton2(@Nullable CharSequence pBtn2Str, @Nullable View.OnClickListener pBtn2Listener) {
            this.mBtn2Bool = true;
            this.mBtn2Str = pBtn2Str;
            this.mBtn2Listener = pBtn2Listener;
            return this;
        }

        /**
         * @param pBtn3Str      为null则默认"忽略"
         * @param pBtn3Listener 为null则默认事件,单击关闭Dialog
         */
        public Builder setButton3(@Nullable CharSequence pBtn3Str, @Nullable View.OnClickListener pBtn3Listener) {
            this.mBtn3Bool = true;
            this.mBtn3Str = pBtn3Str;
            this.mBtn3Listener = pBtn3Listener;
            return this;
        }

        public Dialog build() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.widget_dialog, null);
            final Dialog dialog = new Dialog(mContext, mThemeResId, view);
            // 初始化控件
            LinearLayout dialogLayout = (LinearLayout) view.findViewById(R.id.dialog_layout);
            LinearLayout contentLayout = (LinearLayout) view.findViewById(R.id.dialog_layout_content);
            LinearLayout btnLayout = (LinearLayout) view.findViewById(R.id.dialog_layout_btn);
            ImageView ibClose = (ImageView) view.findViewById(R.id.dialog_close);
            TextView tvTitle = (TextView) view.findViewById(R.id.dialog_tv_title);
            TextView tvMessage = (TextView) view.findViewById(R.id.dialog_tv_message);
            Button btn1 = (Button) view.findViewById(R.id.dialog_btn1);
            Button btn2 = (Button) view.findViewById(R.id.dialog_btn2);
            Button btn3 = (Button) view.findViewById(R.id.dialog_btn3);
            // 填充视图
            dialogLayout.setPadding(mPadding, mPadding, mPadding, mPadding);
            dialogLayout.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHeight));
            contentLayout.setPadding(mContentPadding, mContentPadding, mContentPadding, mContentPadding);
            if (isCloseVisible) {
                ibClose.setVisibility(View.VISIBLE);
            } else {
                ibClose.setVisibility(View.GONE);
            }
            if (mCloseListener == null) {
                ibClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else {
                ibClose.setOnClickListener(mCloseListener);
            }
            CharSequence title = mTitleResId != 0 ? mContext.getString(mTitleResId) : mTitle;
            if (Utils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            }
            View customView = mCustomResId != 0 ? LayoutInflater.from(mContext).inflate(mCustomResId, null) : mCustomView;
            if (customView != null) {
                tvMessage.setVisibility(View.GONE);
                contentLayout.addView(customView);
            } else {
                CharSequence message = mMessageResId != 0 ? mContext.getString(mMessageResId) : mMessage;
                if (Utils.isEmpty(message)) {
                    tvMessage.setVisibility(View.GONE);
                } else {
                    tvMessage.setText(message);
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
                btnWidth = (mWidth - mPadding * 2) / btnNumber;
            }
            if (mBtn1Bool) {
                btn1.setLayoutParams(new LinearLayout.LayoutParams(btnWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!Utils.isEmpty(mBtn1Str)) {
                    btn1.setText(mBtn1Str);
                }
                if (mBtn1Listener != null) {
                    btn1.setOnClickListener(mBtn1Listener);
                } else {
                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                btn1.setVisibility(View.VISIBLE);
            } else {
                btn1.setVisibility(View.GONE);
            }
            if (mBtn2Bool) {
                btn2.setLayoutParams(new LinearLayout.LayoutParams(btnWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!Utils.isEmpty(mBtn2Str)) {
                    btn2.setText(mBtn2Str);
                }
                if (mBtn2Listener != null) {
                    btn2.setOnClickListener(mBtn2Listener);
                } else {
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                btn2.setVisibility(View.VISIBLE);
            } else {
                btn2.setVisibility(View.GONE);
            }
            if (mBtn3Bool) {
                btn3.setLayoutParams(new LinearLayout.LayoutParams(btnWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!Utils.isEmpty(mBtn3Str)) {
                    btn3.setText(mBtn3Str);
                }
                if (mBtn3Listener != null) {
                    btn3.setOnClickListener(mBtn3Listener);
                } else {
                    btn3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                btn3.setVisibility(View.VISIBLE);
            } else {
                btn3.setVisibility(View.GONE);
            }
            // 配置Dialog
            dialog.setContentView(view);
            dialog.setCancelable(isCancelable);
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
            return dialog;
        }
    }

    private View contentView;

    public Dialog(@NonNull Context pContext, @StyleRes int pThemeResId, View pContentView) {
        super(pContext, pThemeResId);
        this.contentView = pContentView;
    }

    /**
     * 右上角叉是否显示,默认显示
     */
    public void setCloseVisible(boolean pIsCloseVisible) {
        ImageView ibClose = (ImageView) contentView.findViewById(R.id.dialog_close);
        if (pIsCloseVisible) {
            ibClose.setVisibility(View.VISIBLE);
        } else {
            ibClose.setVisibility(View.GONE);
        }
    }

    /**
     * 点击右上角叉时回调
     */
    public void setCloseListener(@Nullable View.OnClickListener pCloseListener) {
        ImageView ibClose = (ImageView) contentView.findViewById(R.id.dialog_close);
        if (pCloseListener == null) {
            ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog.this.dismiss();
                }
            });
        } else {
            ibClose.setOnClickListener(pCloseListener);
        }
    }

}
