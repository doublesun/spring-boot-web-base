package com.ywrain.init;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis分页配置
 *
 * @author guangmingxu
 */
@Configuration
public class MyBatisConfig {


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.ywrain.mapper");
        // mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        return mapperScannerConfigurer;
    }

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "false"); // offset作为PageNum使用 - 默认不使用
        p.setProperty("rowBoundsWithCount", "false"); // RowBounds是否进行count查询 - 默认不查询
        p.setProperty("reasonable", "false"); // 分页合理化，默认false
        p.setProperty("supportMethodsArguments", "true"); // 支持接口穿分页参数
        p.setProperty("dialect", "mysql");
        pageHelper.setProperties(p);
        return pageHelper;
    }

}
