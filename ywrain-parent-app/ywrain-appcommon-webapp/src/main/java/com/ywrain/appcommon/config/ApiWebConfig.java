package com.ywrain.appcommon.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ywrain.appcommon.consts.ReqConsts;
import com.ywrain.appcommon.utils.ReqUtil;

/**
 * API web服务的配置
 *
 * @author xuguangming@ywrain.com
 * @date 2017年3月24日
 */
@Configuration
public class ApiWebConfig extends WebMvcConfigurerAdapter {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String ERR_PATH = "/error";
    @Value("${ywrain.showlog.duration.error:1500}")
    private int durationError;
    @Value("${ywrain.showlog.duration.warn:200}")
    private int durationWarn;
    /**
     * 持续时间错误打印, 需要忽略某些接口请求(默认忽略接口: hystrix.stream)
     * <br>比如:
     * <br>hystrix.stream: hystrix dashboard仪表盘默认端点请求, 持续返回数据
     * <br>账号系统高级编辑器扫码使用每次10秒轮询, 不应该打印持续时间错误日志(文档: http://svrapi.docs.suv163.com/web/#/2?page_id=5672)
     */
    @Value("#{'${ywrain.showlog.duration.error.ignore.suffixs:hystrix.stream}'.split(',')}")
    private Set<String> durationErrorIgnoreSuffixs;
    /**
     * 需要持续时间错误打印地址(做成静态属性,无需每次使用durationErrorIgnoreSuffixs进行遍历判断)
     */
    private final Set<String> durationErrorUrls = new HashSet<>();
    /**
     * 忽略持续时间错误打印地址(做成静态属性,无需每次使用durationErrorIgnoreSuffixs进行遍历判断)
     */
    private final Set<String> durationErrorIgnoreUrls = new HashSet<>();

    /**
     * 增加web异常处理页面
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, ERR_PATH);
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, ERR_PATH);
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, ERR_PATH);
            container.addErrorPages(error401Page, error404Page, error500Page);
        };
    }

    /**
     * 跨域访问，允许所有跳转访问
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*"); // 1
        corsConfig.addAllowedHeader("*"); // 2
        corsConfig.addAllowedMethod("*"); // 3
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", corsConfig); // 4
        return new CorsFilter(configSource);
    }


    /**
     * 审计过滤处理
     */
    @Bean
    public Filter traceFilter() {
        return new Filter() {
            @Override
            public void destroy() {
            }

            @Override
            public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
                HttpServletRequest request = (HttpServletRequest) arg0;
                HttpServletResponse response = (HttpServletResponse) arg1;
                long time_start = System.currentTimeMillis();
                if (LOGGER.isDebugEnabled()) {
                    // 添加调试追踪标记
                    String traceId = UUID.randomUUID().toString();
                    response.setHeader(ReqConsts.REQ_FLAG_TRACE_KEY, traceId);
                    request.setAttribute(ReqConsts.REQ_FLAG_TRACE_KEY, traceId);
                }
                chain.doFilter(request, response);
                long duration = System.currentTimeMillis() - time_start;
                showLog(duration, request);
            }

            @Override
            public void init(FilterConfig arg0) {
            }
        };
    }

    /**
     * 超时日志记录打印
     */
    private void showLog(long duration, HttpServletRequest request) {
        if (duration > durationError) {
            // 超过1500ms，错误告警
            String url = request.getRequestURL().toString();
            if (needShowLog(url)) {
                LOGGER.error("(异常)请求耗时: {}毫秒, 请求地址: {}, 参数: {}", duration, url, ReqUtil.paramsToString());
            }
        } else if (duration > durationWarn) {
            // 请求时间大于200ms， 警告
            String url = request.getRequestURL().toString();
            if (needShowLog(url)) {
                LOGGER.warn("(警告)请求耗时: {}毫秒, 请求地址: {}, 参数: {}", duration, url, ReqUtil.paramsToString());
            }
        }
    }

    /**
     * 是否需要打印慢日志
     * <br> 过滤部分请求地址不需要打印慢日志, 比如:
     * <br> hystrix.stream: hystrix dashboard 仪表盘默认端点请求, 持续返回数据, 不应该打印错误日志
     *
     * @param url 请求地址
     * @return true:需要,false不需要
     */
    private boolean needShowLog(String url) {
        if (durationErrorIgnoreUrls.contains(url)) {
            return false;
        }
        if (durationErrorUrls.contains(url)) {
            return true;
        }
        // 没有需要忽略的接口, 则全部都需要打印
        if (durationErrorIgnoreSuffixs == null || durationErrorIgnoreSuffixs.size() == 0) {
            return true;
        } else {
            boolean result = true;
            for (String suf : durationErrorIgnoreSuffixs) {
                if (suf != null) {
                    suf = suf.trim();
                    if (suf.length() > 0 && url.endsWith(suf.trim())) {
                        result = false;
                        break;
                    }
                }
            }
            // 添加到需要或忽略打印日志
            if (result) {
                durationErrorUrls.add(url);
            } else {
                durationErrorIgnoreUrls.add(url);
            }
            return result;
        }
    }
}
