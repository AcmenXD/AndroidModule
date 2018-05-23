package com.acmenxd.retrofit.load;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/7/22 11:12
 * @detail 下载body
 */
public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final IHttpProgress progressListener;
    private BufferedSource bufferedSource;
    private Handler mHandler;

    public ProgressResponseBody(ResponseBody responseBody, IHttpProgress progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            // 当前读取字节数
            long readSize = 0L;
            // 每次读取的长度
            long tempReadSize = 0L;
            // 总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;
            // 下载完成
            boolean isDone = false;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                tempReadSize = super.read(sink, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                readSize += tempReadSize > 0 ? tempReadSize : 0;
                if (mHandler == null) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                        @Override
                        public void call() {
                            mHandler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (progressListener != null && !isDone && readSize <= contentLength) {
                                        isDone = (readSize == contentLength || tempReadSize < 0);
                                        progressListener.progress(isDone, contentLength, readSize);
                                    }
                                }
                            };
                            mHandler.sendEmptyMessage(0);
                        }
                    });
                } else {
                    mHandler.sendEmptyMessage(0);
                }
                return tempReadSize;
            }
        };
    }
}
