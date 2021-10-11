package com.ywrain.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
@Component
public class CronTask {

    private static final Logger log = LoggerFactory.getLogger(CronTask.class);

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        log.warn("cron start");
    }
}
