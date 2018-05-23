package com.acmenxd.retrofit.load;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/7/22 11:12
 * @detail 上传body
 */
public class ProgressRequestBody extends RequestBody {
    private final RequestBody requestBody;
    private final IHttpProgress progressListener;
    private BufferedSink bufferedSink;
    private Handler mHandler;

    public ProgressRequestBody(RequestBody requestBody, IHttpProgress progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            // 当前写入字节数
            long writeSize = 0L;
            // 每次写入的长度
            long tempWriteSize = 0L;
            // 总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;
            // 下载完成
            boolean isDone = false;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                tempWriteSize = byteCount;
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                writeSize += tempWriteSize > 0 ? tempWriteSize : 0;
                if (mHandler == null) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                        @Override
                        public void call() {
                            mHandler = new android.os.Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (progressListener != null && !isDone && writeSize <= contentLength) {
                                        isDone = (writeSize == contentLength || tempWriteSize < 0);
                                        progressListener.progress(isDone, contentLength, writeSize);
                                    }
                                }
                            };
                            mHandler.sendEmptyMessage(0);
                        }
                    });
                } else {
                    mHandler.sendEmptyMessage(0);
                }
            }
        };
    }
}