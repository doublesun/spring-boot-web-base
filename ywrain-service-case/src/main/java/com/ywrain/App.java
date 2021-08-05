package com.ywrain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ywrain.appcommon.converter.AppGsonHttpMessageConverter;

@SpringBootApplication
public class App {

    private final static Logger log = LoggerFactory.getLogger(App.class);

    public final static String defaultDateFormatPattern = "yyyy-MM-dd HH:mm:ss";
    public final static Gson gsonWithoutNull = new GsonBuilder().setDateFormat(defaultDateFormatPattern).create();

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(App.class, args);

        String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            log.info("当前使用profile为: {}", profile);
        }

    }

    /* gson替换spring boot默认的jackson */
    @Bean
    public HttpMessageConverters gsonHttpMessageConverters() {
        AppGsonHttpMessageConverter gsonConverter = new AppGsonHttpMessageConverter();
        gsonConverter.setGson(gsonWithoutNull);
        return new HttpMessageConverters(gsonConverter);
    }
}
