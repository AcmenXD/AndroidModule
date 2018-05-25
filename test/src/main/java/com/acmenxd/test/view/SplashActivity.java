package com.acmenxd.test.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.acmenxd.frame.utils.PermissionsUtils;
import com.acmenxd.frame.utils.StringUtils;
import com.acmenxd.logger.Logger;
import com.acmenxd.test.R;
import com.acmenxd.appbase.base.AppConfig;
import com.acmenxd.appbase.base.BaseActivity;
import com.acmenxd.appbase.base.BaseApplication;
import com.acmenxd.sptool.SpManager;
import com.acmenxd.sptool.SpTool;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/3/31 14:02
 * @detail 启动页
 */
public final class SplashActivity extends BaseActivity {
    private Handler mHandler;
    private boolean nextStarting = false; // 正在启动下一个Activity
    private boolean permissionsOrStartResult = false; // 获取完权限后在启动下个Activity
    private int permissionsIndex = 0; // 当前获取权限的个数
    private int duration = 0; // 启动屏时间
    private boolean isFristInstall = true; // 安装后首次打开
    private String coopenClickUrl; // 广告页
    private String[] permissions = new String[]{ // 应用申请权限列表
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET};

    @Override
    protected void onCreateBefore(@NonNull Bundle savedInstanceState) {
        super.onCreateBefore(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.w("App进入SplashActivity!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SpTool sp = SpManager.getCommonSp(AppConfig.config.SP_Device);
        isFristInstall = sp.getBoolean("isFristInstall", isFristInstall);

        if (isFristInstall) {
            // 打开引导页
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
        } else {
            // 开屏广告
            /*boolean isShowCoopen = false;
            String coopenJson = sp.getString("coopen", null);
            CoopenEntity coopenEntity = null;
            Bitmap bitmap = null;
            if (!Utils.isEmpty(coopenJson)) {
                coopenEntity = new Gson().fromJson(coopenJson, CoopenEntity.class);
            }
            if (coopenEntity != null) {
                String picUrl = MD5.md5(coopenEntity.getPic_url());
                if (!Utils.isEmpty(picUrl)) {
                    bitmap = BitmapUtils.readBitmap(FileUtils.imgCacheDirPath + picUrl, DeviceUtils.getScreenX(this), DeviceUtils.getScreenY(this));
                    if (bitmap != null) {
                        isShowCoopen = true;
                    }
                }
            }
            if (isShowCoopen) {
                // 显示广告
                final ImageView iv = (ImageView) findViewById(R.id.activity_splash_ivImg);
                iv.setImageBitmap(bitmap);
                if (coopenEntity.getClick_able() == 1) {
                    // 进入广告页
                    final String clickUrl = coopenEntity.getClick_url();
                    iv.setOnClickListener(new Utils.OnClickListener() {
                        @Override
                        public void onClick2(View v) {
                            coopenClickUrl = clickUrl;
                            startNextActivity();
                        }
                    });
                }
                duration = coopenEntity.getShowTime() * 1000;
            } else {
                // 默认广告
                duration = 3000;
            }*/
            mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNextActivity();
                }
            }, duration);
        }
        // 获取权限
        requestPermissions();
    }

    /**
     * 获取权限
     */
    private void requestPermissions() {
        permissionsIndex = 0;
        PermissionsUtils.requestPermissions(this, new PermissionsUtils.Callback() {
            @Override
            public void result(@NonNull String permissionName, boolean result, boolean noInquiry) {
                permissionsIndex++;
                if (permissionsIndex == permissions.length) {
                    AppConfig.permissionsAfterInit();
                    // 获取应用整体数据
                    // startService(MainService.class);
                    if (permissionsOrStartResult) {
                        startNextActivity();
                    } else {
                        permissionsOrStartResult = true;
                    }
                }
            }
        }, permissions);
    }

    /**
     * 启动下个Activity
     */
    private void startNextActivity() {
        if (!nextStarting) {
            if (!StringUtils.isEmpty(coopenClickUrl)) {
                /*nextStarting = true;
                Bundle bundle = new Bundle();
                bundle.putString(UrlConstans.WEB_URL, coopenClickUrl);
                startActivity(new Intent(SplashActivity.this, WebActivity.class).putExtras(bundle));
                finish();*/
            } else if (permissionsOrStartResult) {
                nextStarting = true;
                if (isFristInstall) {
                    //startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            } else {
                permissionsOrStartResult = true;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }
}
