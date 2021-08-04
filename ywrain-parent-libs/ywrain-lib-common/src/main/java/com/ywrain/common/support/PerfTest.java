package com.ywrain.common.support;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 循环或并发运行测试器
 *
 * @author weipengfei@youcheyihou.com
 */
public class PerfTest {

    public interface Fun {
        void callback(String funLog);
    }

    public static void run(int loop, String funLog, Fun fun) {
        long stime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            fun.callback(funLog);
        }
        long etime = System.currentTimeMillis();
        System.out.println("[PerfTest*" + loop + "]" + funLog + ": " + (etime - stime) + "ms");
    }

    /**
     * 并行测试
     * @param maxParallel 最大并行线程数
     * @param loop 单个线程内的执行循环数
     * @param funLog 日志记录
     * @param fun 执行匿名方法
     * @param <T> 方法关联类
     * @throws InterruptedException 异常
     */
    public static void runParallel(int maxParallel, int loop, String funLog, Fun fun)
        throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(maxParallel);
        if (executorService != null) {
            CountDownLatch latchStart = new CountDownLatch(1);
            CountDownLatch latchEnd = new CountDownLatch(maxParallel);
            for (int i = 0; i < maxParallel; i++) {
                executorService.submit(() -> {
                    try {
                        latchStart.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    PerfTest.run(loop, "[Parallel*" + maxParallel + "]" + funLog, fun);

                    latchEnd.countDown();
                });
            }

            latchStart.countDown();
            latchEnd.await();

            executorService.shutdown();
        }
    }
}
