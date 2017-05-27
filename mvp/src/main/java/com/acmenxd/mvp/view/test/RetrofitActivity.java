package com.acmenxd.mvp.view.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frame.utils.LoadUtils;
import com.acmenxd.frame.utils.RxUtils;
import com.acmenxd.frame.utils.Utils;
import com.acmenxd.logger.Logger;
import com.acmenxd.retrofit.NetCodeUtils;
import com.acmenxd.retrofit.NetEntity;
import com.acmenxd.retrofit.callback.NetCallback;
import com.acmenxd.retrofit.callback.NetSubscriber;
import com.acmenxd.retrofit.exception.NetException;
import com.acmenxd.retrofit.exception.NetExceptionFail;
import com.acmenxd.retrofit.exception.NetExceptionSuccess;
import com.acmenxd.retrofit.exception.NetExceptionUnknownCode;
import com.acmenxd.mvp.R;
import com.acmenxd.mvp.base.BaseActivity;
import com.acmenxd.mvp.model.response.TestEntity;
import com.acmenxd.mvp.net.IDownloadRequest;
import com.acmenxd.mvp.net.IUploadRequest;
import com.acmenxd.mvp.net.NetCode;

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
    private TestEntity mTestResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getBundle().getString("title"));
        setContentView(R.layout.activity_retrofit);
        iv = (ImageView) findViewById(R.id.imageView);

        /**
         * 同步请求
         * 注意的是网络请求一定要在子线程中完成，不能直接在UI线程执行
         */
        final Call<NetEntity<TestEntity>> call = request().get("token...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final NetEntity<TestEntity> response = call.execute().body();
                    if (response != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTestResponse = response.getData();
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
        request().get("token...").enqueue(
                newCallback(new NetCallback<NetEntity<TestEntity>>() {
                    @Override
                    public void succeed(NetEntity<TestEntity> pData) {
                        int code = pData.getCode();
                        String msg = pData.getMsg();
                        mTestResponse = pData.getData();
                        Logger.i("请求成功 -> url:" + mTestResponse.getUrl());
                    }

                    @Override
                    public void failed(NetException pE) {
                        super.failed(pE);
                        int code = pE.getCode();
                        String msg = pE.getMsg();
                        String tostMsg = pE.getToastMsg();
                    }
                }));
    }

    /**
     * options获取服务器支持的http请求方式
     */
    public void optionsClick(View view) {
        request().options("token...").enqueue(
                newCallback(new NetCallback<NetEntity<TestEntity>>(true) {
                    @Override
                    public void succeed(NetEntity<TestEntity> pData) {
                        NetException exception = new NetCode().parseNetCode(pData.getCode(), pData.getMsg());
                        if (!(exception instanceof NetExceptionSuccess)) {
                            failed(exception); // 异常情况统一交给failed处理
                            return;
                        }
                        mTestResponse = pData.getData();
                        Logger.i("请求成功 -> url:" + mTestResponse.getUrl());
                    }

                    @Override
                    public void failed(NetException pE) {
                        super.failed(pE);
                        int code = pE.getCode();
                        String msg = pE.getMsg();
                        String tostMsg = pE.getToastMsg();
                    }
                }));

    }

    private int code;
    private String msg;
    private NetException mNetException;

    /**
     * post请求
     */
    public void postClick(View view) {
        Subscription subscription = request().post("token...")
                .compose(RxUtils.<NetEntity<TestEntity>>applySchedulers())
                //上面一行代码,等同于下面的这两行代码
                //.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<NetEntity<TestEntity>, TestEntity>() {
                    @Override
                    public TestEntity call(NetEntity<TestEntity> pData) {
                        code = pData.getCode();
                        msg = pData.getMsg();
                        mNetException = new NetCode().parseNetCode(code, msg);
                        return pData.getData();
                    }
                })
                // 这里的true表示请求期间对数据进行过处理,这样Retrofit无法识别处理后的数据,所以需要开发者手动处理错误异常
                .subscribe(newSubscriber(new NetSubscriber<TestEntity>(true) {
                    @Override
                    public void succeed(final TestEntity pData) {
                        // 服务器响应的code和msg统一交给NetCode处理
                        NetException exception = new NetCode().parseNetCode(code, msg);
                        NetCodeUtils.netCodeResult(exception, new NetCodeUtils.NetCodeCallback() {
                            @Override
                            public void successData(NetExceptionSuccess pE) {
                                mTestResponse = pData;
                                Logger.i("请求成功 -> url:" + mTestResponse.getUrl());
                            }

                            @Override
                            public void errorData(NetExceptionFail pE) {
                                failed(pE);
                            }

                            @Override
                            public void unknownCode(NetExceptionUnknownCode pE) {
                                failed(pE);
                            }
                        });
                    }

                    @Override
                    public void failed(NetException pE) {
                        super.failed(pE);
                        int code = pE.getCode();
                        String msg = pE.getMsg();
                        String tostMsg = pE.getToastMsg();
                    }

                    @Override
                    public void finished() {

                    }
                }, true));
        addSubscriptions(subscription);
    }

    /**
     * put用法同post主要用于创建资源
     */
    public void putClick(View view) {
        request().put("token...", mTestResponse).enqueue(
                newCallback(new NetCallback<NetEntity<TestEntity>>() {
                    @Override
                    public void succeed(NetEntity<TestEntity> pData) {
                        int code = pData.getCode();
                        String msg = pData.getMsg();
                        mTestResponse = pData.getData();
                        Logger.i("请求成功 -> url:" + mTestResponse.getUrl());
                    }

                    @Override
                    public void failed(NetException pE) {
                        super.failed(pE);
                        int code = pE.getCode();
                        String msg = pE.getMsg();
                        String tostMsg = pE.getToastMsg();
                    }
                }));
    }

    /**
     * 下载请求
     */
    public void downloadClick(View view) {
        request(IDownloadRequest.class).download("http://server.jeasonlzy.com/OkHttpUtils/image")
//        request(IDownloadRequest.class).download("http://10.1.22.49:8080/webDemo/aa")
                .compose(RxUtils.<ResponseBody>applySchedulers())
                .subscribe(newSubscriber(new NetSubscriber<ResponseBody>() {
                    @Override
                    public void succeed(ResponseBody pData) {
                        boolean result = LoadUtils.saveDownLoadFile(pData, FileUtils.cacheDirPath + "download.jpg");
                    }
                }));
    }

    /**
     * bitmap请求
     */
    public void bitmapClick(View view) {
        request().image("token...").enqueue(
                newCallback(new NetCallback<Bitmap>() {
                    @Override
                    public void succeed(Bitmap pBitmap) {
                        iv.setImageBitmap(pBitmap);
                    }
                }));
    }

    /**
     * bitmap上传请求
     */
    public void upBitmapClick(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        request().upImage("token...", bitmap).enqueue(
                newCallback(new NetCallback<NetEntity>() {
                    @Override
                    public void succeed(NetEntity pData) {
                        int code = pData.getCode();
                        String msg = pData.getMsg();
                        Logger.i("请求成功 -> msg:" + pData.getMsg());
                    }
                }));
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
        File file = new File(path);

        HashMap<String, String> dataStrs = new HashMap<>();
        dataStrs.put("name_key_1", "name_value_1");
        dataStrs.put("name_key_2", "name_value_2");
        request(IUploadRequest.class)
                .upload(LoadUtils.getDataStrs(dataStrs), LoadUtils.getDataFiles(file, file)) // 第一种方法
//                .upload(NetUtils.getRequestBody(dataStrs, file, file)) // 第二种方法
                .compose(RxUtils.<NetEntity>applySchedulers())
                .subscribe(newSubscriber(new NetSubscriber<NetEntity>() {
                    @Override
                    public void succeed(NetEntity pData) {
                        int code = pData.getCode();
                        String msg = pData.getMsg();
                        Logger.i("请求成功 -> msg:" + pData.getMsg());
                    }

                    @Override
                    public void failed(NetException pE) {
                        super.failed(pE);
                        int code = pE.getCode();
                        String msg = pE.getMsg();
                        String tostMsg = pE.getToastMsg();
                    }
                }));
    }
}
