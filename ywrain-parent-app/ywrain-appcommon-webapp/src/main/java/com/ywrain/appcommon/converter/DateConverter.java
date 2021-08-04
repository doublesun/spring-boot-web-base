/**
 * 〈一句话功能简述〉
 */
package com.ywrain.appcommon.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

/**
 * @Title: DateConverterConfiguration.java
 * @Description: 接收时间参数转换
 * @author 许光明
 * @date 2017年3月24日 下午8:50:04
 * @version V1.0
 */
@Configuration
public class DateConverter {

    /**
     * 接受时间参数转换
     */
    @Bean
    public Converter<String, Date> addNewConvert() {
        return new Converter<String, Date>() {
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse((String) source);
                } catch (ParseException e) {
                    return null;
                }

                return date;
            }
        };
    }

}
