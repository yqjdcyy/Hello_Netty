package com.yao.Future;

import java.util.concurrent.*;

/**
 * Created by Yao on 2015/1/4.
 */
public class FutureExample {

    public static void main(String[] args) throws Exception {

        ExecutorService executor =
                new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                        60L, TimeUnit.SECONDS,
                        new SynchronousQueue<Runnable>());
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                //do something
                System.out.println("i am task1.....");
                System.out.println("i am task1.....");
            }
        };
        Callable<Integer> task2 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                //do something
                return new Integer(100);
            }
        };
        Future<?> f1 = executor.submit(task1);
        Future<Integer> f2 = executor.submit(task2);
        System.out.println("task1 is completed? " + f1.isDone());
        System.out.println("task2 is completed? " + f2.isDone());
        //waiting task1 completed
        while (f1.isDone()) {
            System.out.println("task1 completed.");
            break;
        }
        //waiting task2 completed
        while (f2.isDone()) {
            System.out.println("return value by task2: " + f2.get());
            break;
        }
    }

}