package com.ywrain.appcommon.beans;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ywrain.appcommon.auth.ApiAuth;
import com.ywrain.appcommon.auth.JwtApiAuth;

/**
 * ywrain-appcommon-webapp提供的bean声明 <br>
 */
@Configuration
public class CommonBean {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Value("${ywrain.executor.common.pool-size:10}")
    private int poolSize;
    @Value("${ywrain.executor.common.pool-max-size:50}")
    private int poolMaxSize;
    @Value("${ywrain.executor.common.queue-capacity:100}")
    private int queueCapacity;
    @Value("${ywrain.executor.common.wait-for-tasks-to-complete-on-shutdown:false}")
    private boolean waitForTasksToCompleteOnShutdown;
    @Value("${ywrain.executor.common.await-termination-seconds:0}")
    private int awaitTerminationSeconds;

    /**
     * API接口鉴权业务实现，默认使用JWT方案
     */
    @Bean
    @ConditionalOnMissingBean(ApiAuth.class)
    public ApiAuth getApiAuth() {
        return new JwtApiAuth();
    }

    /**
     * 通用线程池配置
     * 
     * <p>
     * 拒绝策略：切回当前线程执行任务
     * </p>
     * <p>
     * 使用时指定名称，如： @Async
     * </p>
     * 
     * @return Executor
     */
    @Bean(name = "commonExecutor")
    public Executor commonExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolMaxSize);
        executor.setQueueCapacity(queueCapacity);
        // 开启线程任务的关闭等待
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        // 默认使用beanName
        executor.setThreadNamePrefix("IYCCommonPool-");
        // rejection-policy：当pool已经达到max size的时候，由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new MyRejectedCallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 拒绝线程处理类 <br>
     */
    private class MyRejectedCallerRunsPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code CallerRunsPolicy}.
         */
        public MyRejectedCallerRunsPolicy() {}

        /**
         * Executes task r in the caller's thread, unless the executor has been shut down, in which case the task is discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            LOGGER.warn("线程池任务拒绝执行，返回当前线程执行，任务：{}", r.toString());
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }
}
