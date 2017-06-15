package com.acmenxd.frame.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/15 15:10
 * @detail view工具类
 */
public final class ViewUtils {
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

}
