package com.acmenxd.bourse.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.acmenxd.bourse.R;
import com.acmenxd.bourse.base.BaseApplication;
import com.acmenxd.frame.utils.PermissionsUtils;
import com.acmenxd.logger.Logger;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/31 14:02
 * @detail 启动页
 */
public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;
    private int duration = 2000; // 启动屏时间
    private boolean permissionsOrStartResult = false; // 获取完权限后在启动下个Activity
    private String[] permissions = new String[]{ // 应用申请权限列表
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Logger.w("App进入SplashActivity!");

        long time = 0;
        if (BaseApplication.instance().startTime > 0) {
            time = System.currentTimeMillis() - BaseApplication.instance().startTime;
            BaseApplication.instance().startTime = 0;
        }
        if (time > duration) {
            startNextActivity();
        } else {
            mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNextActivity();
                }
            }, duration - time);
        }

        PermissionsUtils.requestPermissions(this, new PermissionsUtils.CallbackGroup() {
            @Override
            public void result(boolean result) {
                if (permissionsOrStartResult) {
                    startNextActivity();
                } else {
                    permissionsOrStartResult = true;
                }
            }
        }, permissions);
    }

    /**
     * 启动下个Activity
     */
    private void startNextActivity() {
        if (permissionsOrStartResult) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            permissionsOrStartResult = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
