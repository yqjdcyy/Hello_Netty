package com.yao.callbacks;

/**
 * Created by Administrator on 2015/1/4.
 */
public interface FetcherCallback {
    void onData(Data data) throws Exception;
    void onError(Throwable cause);
}
