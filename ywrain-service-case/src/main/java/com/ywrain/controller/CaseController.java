package com.ywrain.controller;

import com.google.common.collect.Maps;
import com.ywrain.appcommon.proto.Response;
import com.ywrain.appcommon.utils.ExceptionUtil;
import com.ywrain.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/case")
public class CaseController {

    /**
     * 测试时间类型的接收和响应，默认格式：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    @RequestMapping("/test_date")
    public Response testDate(Date date) {
        return Response.ok(date);
    }

    /**
     * 默认过滤响应的null值
     *
     * @return
     */
    @RequestMapping("/test_null_respose")
    public Response testNullResponse() {
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("name", "guangming");
        ret.put("passsword", null);
        return Response.ok(ret);
    }

    /**
     * 异常处理
     *
     * @return
     */
    @RequestMapping("/test_exception")
    public Response testException() {
        throw ExceptionUtil.getAuthException("登录异常");
    }

    /**
     * 慢日志警告
     * <p>
     * 默认超过200ms打印警告日志
     * <p>
     * 配置参考：ywrain.showlog.duration.warn=200
     *
     * @return
     */
    @RequestMapping("/test_showlog_warn")
    public Response testShowlog() throws InterruptedException {
        Thread.sleep(300);
        return Response.ok();
    }

    /**
     * 慢日志错误打印
     * <p>
     * 默认超过1500ms打印警告日志
     * <p>
     * 配置参考：ywrain.showlog.duration.error=1500
     *
     * @return
     */
    @RequestMapping("/test_showlog_error")
    public Response testShowlogError() throws InterruptedException {
        Thread.sleep(2000);
        return Response.ok();
    }

    @Autowired
    private ActionService actionService;

    /**
     * 事务测试
     */
    @RequestMapping("/tranction")
    public void tranction() {
        actionService.transcation();
    }
}
