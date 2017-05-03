package com.acmenxd.mvp.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import com.acmenxd.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/23 16:09
 * @detail Bitmap工具类
 */
public class BitmapUtils {

    /**
     * 截取屏幕(全屏)并保存
     *
     * @param pActivity
     * @param isSaveStatusBar 是否保留状态栏
     * @param pSavePath       保存路径
     */
    public static void saveScreenAsImage(Activity pActivity, boolean isSaveStatusBar, File pSavePath) {
        View pView = pActivity.getWindow().getDecorView();
        pView.setDrawingCacheEnabled(true);
        pView.buildDrawingCache();
        Bitmap srcBitmap = pView.getDrawingCache();
        // 图片大小
        Point point = new Point();
        pActivity.getWindowManager().getDefaultDisplay().getSize(point);
        int width = pView.getWidth();//point.x;
        int height = pView.getHeight();//point.y;
        // 标题栏高度
        int statusBarHeight = 0;
        if (isSaveStatusBar == false) {
            Rect frame = new Rect();
            pActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
            height = height - statusBarHeight;
        }
        // 创建存储的bitmap
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, statusBarHeight, width, height);
        // 清理掉缓存图片,防止内存溢出
        pView.destroyDrawingCache();
        // 保存图片
        saveBitmap(bitmap, pSavePath);
    }

    /**
     * 保存图片到存储盘 -> 创建临时文件名称
     * * 图片名为随机的 JPEG 格式图片
     */
    public static void saveBitmap(Bitmap pBitmap, File savePath) {
        saveBitmap(pBitmap, savePath, String.valueOf(RandomUtils.getRandomByTime()) + ".jpg", null);
    }

    /**
     * 保存图片到存储盘
     *
     * @param pBitmap  存储的bitmap对象
     * @param savePath 存储路径
     * @param fileName 文件名 (带后缀名)
     * @param format   图片格式 (默认 JPEG 格式)
     */
    public static void saveBitmap(Bitmap pBitmap, File savePath, String fileName, Bitmap.CompressFormat format) {
        if (format == null) {
            format = Bitmap.CompressFormat.JPEG;
        }
        // 保存图片
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(savePath, fileName));
            if (fos != null) {
                pBitmap.compress(format, 100, fos);
                fos.flush();
            }
        } catch (IOException pE) {
            Logger.e(pE);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

}
