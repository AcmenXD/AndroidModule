package com.acmenxd.testbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.acmenxd.frame.basis.ActivityStackManager;
import com.acmenxd.frame.basis.FrameActivity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2018/5/26 11:00
 * @detail 辅助模块间互调
 */
public class ModuleTogether {
    public static String DBActivity = "com.acmenxd.test_demo.view.DBActivity";

    /**
     * 打开数据库UI
     */
    public static void openDBActivity() {
        open(null, DBActivity);
    }

    private static void open(Bundle bundle, String className) {
        Activity activity = ActivityStackManager.INSTANCE.currentActivity();
        Class c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException pE) {
            pE.printStackTrace();
        }
        if (c != null) {
            if (activity instanceof FrameActivity) {
                if (bundle == null) {
                    ((FrameActivity) activity).startActivity(c);
                } else {
                    ((FrameActivity) activity).startActivity(c, bundle);
                }
            } else {
                Intent intent = new Intent(activity, c);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                activity.startActivity(intent);
            }
        }
    }
}
