package com.acmenxd.test_demo.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.logger.Logger;
import com.acmenxd.test_demo.R;
import com.acmenxd.appbase.base.BaseActivity;
import com.acmenxd.appbase.utils.RefreshUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/16 15:34
 * @detail something
 */
public class FileActivity extends BaseActivity {
    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        setTitleView(R.layout.layout_title);
        RefreshUtils.initTitleView(getTitleView(), getBundle().getString("title"), new RefreshUtils.OnTitleListener() {
            @Override
            public void onBack() {
                FileActivity.this.finish();
            }
        });

        /**
         * File文件操作
         */
        // 判断文件是否存在
        Logger.v(FileUtils.isExists(FileUtils.apkSaveDirPath + "copy.txt"));
        try {
            // 列出目录中的文件
            File[] files = FileUtils.getFiles(FileUtils.apkSaveDir);
            for (File file : files) {
                Logger.v(file.getAbsolutePath());
            }
        } catch (IOException pE) {
            Logger.e(pE);
        }
        try {
            // 创建目录
            FileUtils.createDirectorys(FileUtils.apkSaveDirPath + "test");
            FileUtils.createDirectorys(FileUtils.apkSaveDirPath + "test1/test2/test3/test4");
            // 创建文件
            FileUtils.createFile(FileUtils.apkSaveDirPath + "copy.txt");
            FileUtils.createFile(FileUtils.apkSaveDirPath + "move.txt");
            // 创建并覆盖文件
            FileUtils.createFileWithDelete(FileUtils.apkSaveDirPath + "file.txt");
            // 移动文件
            FileUtils.moveFile(new File(FileUtils.apkSaveDirPath + "move.txt"), new File(FileUtils.apkSaveDirPath + "move/move_finish.txt"), true);
            // 拷贝文件
            FileUtils.copyFile(new File(FileUtils.apkSaveDirPath + "copy.txt"), new File(FileUtils.apkSaveDirPath + "copy/copy_finish.txt"), true);
        } catch (IOException pE) {
            Logger.e(pE);
        }
        /**
         * 提示语
         */
        TextView tv = new TextView(this);
        tv.setTextSize(40);
        tv.setTextColor(Color.BLACK);
        tv.setText("操作成功");
        LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        pa.gravity = Gravity.CENTER;
        ((LinearLayout)getContentView()).addView(tv, pa);
    }
}
