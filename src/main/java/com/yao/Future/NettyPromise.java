package com.yao.Future;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.TimeUnit;

/**
 * @author qingju.yao
 * @date 2020/07/25
 */
public class NettyPromise {

    public static void main(String[] args) throws InterruptedException {

        DefaultEventExecutor executor = new DefaultEventExecutor();
        DefaultPromise<Boolean> promise = new DefaultPromise<>(executor);
        promise.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Future.success");
            } else if (future.isCancelled()) {
                System.out.println("Future.cancel");
            }
        }).addListener(future -> {
            System.out.println("Future.last listener finish");
        });

        executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                throw new IllegalStateException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                promise.setFailure(e);
                return;
            }
            promise.setSuccess(true);
        });

        promise.sync();
    }
}
