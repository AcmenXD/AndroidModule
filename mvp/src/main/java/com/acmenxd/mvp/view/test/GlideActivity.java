package com.acmenxd.mvp.view.test;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.acmenxd.glide.CircleTransform;
import com.acmenxd.glide.GlideManager;
import com.acmenxd.glide.RoundTransform;
import com.acmenxd.glide.SaveCallback;
import com.acmenxd.mvp.R;
import com.acmenxd.mvp.base.BaseActivity;
import com.acmenxd.mvp.utils.ViewUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/3 15:39
 * @detail something
 */
public class GlideActivity extends BaseActivity {
    private ImageView iv;
    private String url = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2994123101,43895145&fm=23&gp=0.jpg";

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        setTitleView(R.layout.layout_title);
        ViewUtils.initTitleView(getTitleView(), getBundle().getString("title"), new ViewUtils.OnTitleListener() {
            @Override
            public void onBack() {
                GlideActivity.this.finish();
            }
        });
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void btnClick1(View view) {
        url = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2994123101,43895145&fm=23&gp=0.jpg";
        Glide.with(this)
                .load(url)
                .placeholder(R.mipmap.ic_launcher) // 占位符（默认显示图片）
                .error(R.mipmap.ic_launcher) // 错误占位符（当网址错误，或其他原因显示不出来图片时）
                .crossFade() // 渐变动画（淡入淡出效果，默认是300毫秒，当然后边会有自定义动画）
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .transform(new RoundTransform(this)) //圆角转化器
                .into(iv);
    }

    public void btnClick2(View view) {
        url = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2994123101,43895145&fm=23&gp=0.jpg";
        Glide.with(this)
                .load(url)
                .placeholder(R.mipmap.ic_launcher) // 占位符（默认显示图片）
                .error(R.mipmap.ic_launcher) // 错误占位符（当网址错误，或其他原因显示不出来图片时）
                .crossFade() // 渐变动画（淡入淡出效果，默认是300毫秒，当然后边会有自定义动画）
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .transform(new CircleTransform(this)) //圆角转化器
                .into(iv);
    }

    public void btnClick3(View view) {
        url = "http://ww2.sinaimg.cn/mw690/c2adb464tw1eb6k8q1yflg20dw07hqv5.gif";
        Glide.with(this)
                .load(url)
                .asGif()//显示gif动态图片
                .placeholder(R.mipmap.ic_launcher) // 占位符（默认显示图片）
                .error(R.mipmap.ic_launcher) // 错误占位符（当网址错误，或其他原因显示不出来图片时）
                .crossFade() // 渐变动画（淡入淡出效果，默认是300毫秒，当然后边会有自定义动画）
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .fitCenter()
                .into(iv);
    }

    public void btnClick4(View view) {
        File save = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/save.png");
        GlideManager.saveImage(this, url, save, new SaveCallback() {
            @Override
            public void succeed(final File file) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GlideActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void failed(final Exception pE) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GlideActivity.this, pE.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void doc() {
        Glide.with(this).load(url)
                .placeholder(R.mipmap.ic_launcher) // 占位符（默认显示图片）
                .error(R.mipmap.ic_launcher)       // 错误占位符（当网址错误，或其他原因显示不出来图片时）
                .into(iv)
//                .fitCenter()
//                .centerCrop()
//                .asBitmap()  //显示gif静态图片
//                .asGif()     //显示gif动态图片
//                .animate(R.anim.item_alpha_in) //设置动画
//                .crossFade()       // 渐变动画（淡入淡出效果，默认是300毫秒，当然后边会有自定义动画）
//                .dontAnimate()   // 不想要任何动画效果
//                .thumbnail(0.1f) //设置缩略图支持
//                .override(800, 800)//设置加载尺寸
//                .skipMemoryCache(true) //跳过内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
//                .priority(Priority.NORMAL) //下载优先级
//                .transform(new RoundTransform(this)) //圆角转化器
//                .transform(new CircleTransform(this)) //圆型转化器
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        // 可以用于监控请求发生错误来源
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        imageView.setImageDrawable(resource);
//                        return false;
//                    }
//                })
//                .into(new SimpleTarget<GlideDrawable>() { //先下载图片然后再做一些合成的功能
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        imageView.setImageDrawable(resource);
//                    }
//                });
        ;
    }
}
