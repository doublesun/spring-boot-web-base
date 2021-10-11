package com.ywrain.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class OneStartup implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OneStartup.class);

    @Override
    public void run(String... args) throws Exception {
        Thread.sleep(2000);
        log.warn("order 1");
    }
}
