package com.acmenxd.mvp.view.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frame.utils.RxUtils;
import com.acmenxd.frame.utils.Utils;
import com.acmenxd.logger.Logger;
import com.acmenxd.mvp.R;
import com.acmenxd.mvp.base.BaseActivity;
import com.acmenxd.mvp.http.IDownloadRequest;
import com.acmenxd.mvp.http.IUploadRequest;
import com.acmenxd.mvp.http.LoadHelper;
import com.acmenxd.mvp.model.response.TestEntity;
import com.acmenxd.mvp.utils.ViewUtils;
import com.acmenxd.retrofit.HttpEntity;
import com.acmenxd.retrofit.HttpGenericityEntity;
import com.acmenxd.retrofit.exception.HttpException;
import com.acmenxd.toaster.Toaster;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/16 15:34
 * @detail something
 */
public class RetrofitActivity extends BaseActivity {
    private final String TAG = this.getClass().getName();
    private ImageView iv;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        setTitleView(R.layout.layout_title);
        ViewUtils.initTitleView(getTitleView(), getBundle().getString("title"), new ViewUtils.OnTitleListener() {
            @Override
            public void onBack() {
                RetrofitActivity.this.finish();
            }
        });
        iv = (ImageView) findViewById(R.id.imageView);

        /**
         * 同步请求
         * 注意的是网络请求一定要在子线程中完成，不能直接在UI线程执行
         */
        final Call<TestEntity> call = request().get("param...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final TestEntity response = call.execute().body();
                    if (response != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
            }
        });
    }

    /**
     * get请求
     */
    public void getClick(View view) {
        request().get("param...")
                .enqueue(new BindCallback<TestEntity>() {
                    @Override
                    public void succeed(@NonNull TestEntity pData) {
                        int code = pData.getCode();
                        String msg = pData.getMsg();
                        Logger.i("请求成功 -> url:" + pData.data.url);
                    }

                    @Override
                    public void finished() {
                    }
                });
    }

    /**
     * options获取服务器支持的http请求方式
     */
    public void optionsClick(View view) {
        request().options("param...")
                .enqueue(new BindCallback<TestEntity>() {
                    @Override
                    public void succeed(@NonNull TestEntity pData) {
                        Logger.i("请求成功 -> url:");
                    }

                    @Override
                    public void finished() {
                    }
                });

    }

    private int code;
    private String msg;
    private HttpException mNetException;

    /**
     * post请求
     */
    public void postClick(View view) {
        Subscription subscription = request().postRx("param...")
                .compose(RxUtils.<HttpGenericityEntity<TestEntity>>applySchedulers())
                //上面一行代码,等同于下面的这两行代码
                //.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<HttpGenericityEntity<TestEntity>, TestEntity>() {
                    @Override
                    public TestEntity call(HttpGenericityEntity<TestEntity> pData) {
                        code = pData.getCode();
                        msg = pData.getMsg();
                        return pData.getData();
                    }
                })
                // 这里的true表示请求期间对数据进行过处理,这样Retrofit无法识别处理后的数据,所以需要开发者手动处理错误异常
                .subscribe(new BindSubscriber<TestEntity>() {
                    @Override
                    public void succeed(final @NonNull TestEntity pData) {

                    }

                    @Override
                    public void finished() {
                    }
                });
        addSubscriptions(subscription);
    }

    /**
     * put用法同post主要用于创建资源
     */
    public void putClick(View view) {
        request().put("param...", new TestEntity())
                .enqueue(new BindCallback<HttpGenericityEntity<TestEntity>>() {
                    @Override
                    public void succeed(@NonNull HttpGenericityEntity<TestEntity> pData) {
                        int code = pData.getCode();
                        String msg = pData.getMsg();
                        Logger.i("请求成功 -> url:");
                    }

                    @Override
                    public void finished() {
                    }
                });
    }

    /**
     * 下载请求
     */
    public void downloadClick(View view) {
        request(IDownloadRequest.class).getRx("http://server.jeasonlzy.com/OkHttpUtils/image")
//        request(IDownloadRequest.class).download("http://10.1.22.49:8080/webDemo/aa")
                .compose(RxUtils.<ResponseBody>applySchedulers())
                .subscribe(new BindSubscriber<ResponseBody>() {
                    @Override
                    public void succeed(@NonNull ResponseBody pData) {
                        boolean result = LoadHelper.saveDownLoadFile(pData, FileUtils.cacheDirPath + "download.jpg");
                    }
                });
//        request(IDownloadRequest.class).download("http://server.jeasonlzy.com/OkHttpUtils/image")
//                .enqueue(newCallback(new NetCallback<ResponseBody>() {
//                    @Override
//                    public void succeed(ResponseBody pData) {
//                        boolean result = LoadUtils.saveDownLoadFile(pData, FileUtils.cacheDirPath + "download.jpg");
//                    }
//                }));
    }

    /**
     * bitmap请求
     */
    public void bitmapClick(View view) {
        request().downImage("param...")
                .enqueue(new BindCallback<Bitmap>() {
                    @Override
                    public void succeed(@NonNull Bitmap pData) {
                        iv.setImageBitmap(pData);
                    }
                });
    }

    /**
     * bitmap上传请求
     */
    public void upBitmapClick(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        request().upImage("param...", bitmap)
                .enqueue(new BindCallback<HttpEntity>() {
                    @Override
                    public void succeed(@NonNull HttpEntity pData) {
                        int code = pData.getCode();
                        String msg = pData.getMsg();
                        Logger.i("请求成功 -> msg:" + pData.getMsg());
                    }
                });
    }

    /**
     * 上传
     */
    public void uploadClick(View view) {
        Utils.showFileChooser(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = Utils.showFileChooser_onActivityResult(this, requestCode, resultCode, data);
        if (Utils.isEmpty(path)) {
            Toaster.show("文件获取失败!");
            return;
        }
        File file = new File(path);

        HashMap<String, String> dataStrs = new HashMap<>();
        dataStrs.put("name_key_1", "name_value_1");
        dataStrs.put("name_key_2", "name_value_2");
        request(IUploadRequest.class)
                .uploadRx("http://server.jeasonlzy.com/OkHttpUtils/" + "upload", LoadHelper.getUploadFiles(file, file), LoadHelper.getUploadParams(dataStrs))// 第一种方法
//                .upload("http://server.jeasonlzy.com/OkHttpUtils/" + "upload", LoadHelper.getRequestBody(dataStrs, file, file)) // 第二种方法
                .compose(RxUtils.<HttpEntity>applySchedulers())
                .subscribe(new BindSubscriber<HttpEntity>() {
                    @Override
                    public void succeed(@NonNull HttpEntity pData) {
                        int code = pData.getCode();
                        String msg = pData.getMsg();
                        Logger.i("请求成功 -> msg:" + pData.getMsg());
                    }

                    @Override
                    public void finished() {
                    }
                });
    }
}
