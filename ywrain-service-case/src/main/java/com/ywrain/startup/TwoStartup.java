package com.ywrain.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动后运行
 *
 * order 启动顺序，正序执行
 */
@Component
@Order(value = 2)
public class TwoStartup implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TwoStartup.class);

    @Override
    public void run(String... args) throws Exception {

        Thread.sleep(2000);
        log.warn("order 2");
    }
}
