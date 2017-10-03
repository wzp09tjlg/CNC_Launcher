package cn.cncgroup.tv.utils;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangjifeng on 15/5/4.
 */
public final class ThreadPool {
    private static final String TAG = "ThreadPool";
	private static ThreadPoolExecutor executor = null;

    public static void initialize() {
        //核心线程数5个，最大线程数10个，如果一个线程空闲超过30秒则终止
        executor = new ThreadPoolExecutor(5, 10,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        //调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0
        executor.allowCoreThreadTimeOut(true);
    }

    /**
     * 往线程池中添加任务
     * @param runnable 任务
     */
    public static void execute(Runnable runnable) {
        Log.i(TAG, "线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                executor.getQueue().size() + "，已执行完别的任务数目：" + executor.getCompletedTaskCount());
        executor.execute(runnable);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
