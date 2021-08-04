package com.ywrain.appcommon.config;

import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;

import com.ywrain.appcommon.utils.ExceptionUtil;

/**
 * 线程池配置 异步
 *
 * @author xuguangming@ywrain.com
 * @date 2017年3月24日
 **/
@Component
public class AsyncPoolConfigurer implements AsyncConfigurer {
    private Logger LOGGER = LoggerFactory.getLogger(AsyncPoolConfigurer.class);

    @Resource(name = "commonExecutor")
    Executor commonExecutor;

    /**
     * 配置@Async线程池，使用通用线程池配置
     */
    @Override
    public Executor getAsyncExecutor() {
        return commonExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> LOGGER.error("异步线程执行任务{}异常: {}", method.getName(), ExceptionUtil.getStackTrace(ex));
    }
}
