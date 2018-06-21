package com.acmenxd.demo.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.logger.LogTag;
import com.acmenxd.logger.Logger;
import com.acmenxd.demo.R;
import com.acmenxd.core.base.AppConfig;
import com.acmenxd.core.base.BaseActivity;
import com.acmenxd.core.utils.RefreshUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/16 15:34
 * @detail something
 */
public class LogActivity extends BaseActivity {
    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        Logger.w("App进入LogActivity!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        setTitleView(R.layout.layout_title);
        RefreshUtils.initTitleView(getTitleView(), getBundle().getString("title"), new RefreshUtils.OnTitleListener() {
            @Override
            public void onBack() {
                LogActivity.this.finish();
            }
        });

        /**
         * 日志输出
         */
        Logger.v("test1");
        Logger.d("test1", "test2");
        Logger.i(LogTag.mk("LogActivity"), "test1", "test2");
        Logger.w(new NullPointerException());
        Logger.e(LogTag.mk("LogActivity"), new NullPointerException());
        Logger.a(new NullPointerException(), "test1", "test2");
        Logger.a(LogTag.mk("LogActivity"), new NullPointerException(), "test1", "test2");
        /**
         * json格式输出
         */
        InputStreamReader in = new InputStreamReader(getResources().openRawResource(R.raw.log_json));
        char[] b = new char[1024];
        String str = "";
        int index = 0;
        try {
            while ((index = in.read(b)) != -1) {
                str += new String(b, 0, index);
            }
            in.close();
        } catch (IOException pE) {
            Logger.e(pE);
        }
        Logger.json(LogTag.mk("jsonLogActivity"), str.toString());
        /**
         * xml格式输出
         */
        in = new InputStreamReader(getResources().openRawResource(R.raw.log_xml));
        b = new char[1024];
        str = "";
        index = 0;
        try {
            while ((index = in.read(b)) != -1) {
                str += new String(b, 0, index);
            }
            in.close();
        } catch (IOException pE) {
            Logger.e(pE);
        }
        Logger.xml(LogTag.mk("xmlLogActivity"), str.toString());
        /**
         * file格式输出 - 输出到本地文件
         */
        File dir = new File(AppConfig.config.LOG_DIR);
        Logger.file("test1");
        Logger.file("test1", "test2");
        Logger.file(LogTag.mk("fileLogActivity"), "test1", "test2");
        Logger.file(LogTag.mk("fileLogActivity"), "", dir, "test1", "test2");
        Logger.file(LogTag.mk("fileLogActivity"), "LogTest.txt", dir, "test1", "test2");

        Logger.file(new NullPointerException());
        Logger.file(LogTag.mk("fileLogActivity"), new NullPointerException());
        Logger.file(LogTag.mk("fileLogActivity"), "", new NullPointerException());
        Logger.file(LogTag.mk("fileLogActivity"), null, new NullPointerException());
        Logger.file(LogTag.mk("fileLogActivity"), "LogTest.txt", new NullPointerException());
        Logger.file(LogTag.mk("fileLogActivity"), "LogTest.txt", dir, new NullPointerException());

        Logger.file(new NullPointerException(), "test1");
        Logger.file(new NullPointerException(), "test1", "test2");
        Logger.file(LogTag.mk("fileLogActivity"), new NullPointerException(), "test1", "test2");
        Logger.file(LogTag.mk("fileLogActivity"), "LogTest.txt", new NullPointerException(), "test1", "test2");
        Logger.file(LogTag.mk("fileLogActivity"), "LogTest.txt", dir, new NullPointerException(), "test1", "test2");
        /**
         * 提示语
         */
        TextView tv = new TextView(this);
        tv.setTextSize(40);
        tv.setTextColor(Color.BLACK);
        tv.setText("日志输出成功");
        LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        pa.gravity = Gravity.CENTER;
        ((LinearLayout) getContentView()).addView(tv, pa);
    }
}
