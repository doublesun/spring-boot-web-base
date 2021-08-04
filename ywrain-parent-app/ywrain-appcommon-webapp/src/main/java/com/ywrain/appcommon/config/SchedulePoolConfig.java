package com.ywrain.appcommon.config;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 配置计划任务Scheduled的线程池
 * <pre>
 *  配置线程池后，同个计划任务触发后会等待上一个任务执行完成
 *  默认线程池线程数为3(ywrain.executor.scheduled.pool-size=3)
 * </pre>
 *
 * @author xuguangming@ywrain.com
 * @date 2018年3月15日
 **/
@Configuration
@EnableScheduling
public class SchedulePoolConfig implements SchedulingConfigurer {

    @Value("${ywrain.executor.scheduled.pool-size:3}")
    private int poolSize;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        AtomicInteger number = new AtomicInteger(1);
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(poolSize, r -> {
            Thread thread = new Thread(r);
            thread.setName("IYCSchedulePool-" + number.getAndIncrement());
            return thread;
        }));
    }

}
