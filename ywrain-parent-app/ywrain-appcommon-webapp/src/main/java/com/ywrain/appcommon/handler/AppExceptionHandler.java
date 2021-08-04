package com.ywrain.appcommon.handler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ywrain.appcommon.consts.ProtoConsts;
import com.ywrain.appcommon.consts.ReqConsts;
import com.ywrain.appcommon.exception.AppAuthException;
import com.ywrain.appcommon.exception.AppBizException;
import com.ywrain.appcommon.exception.AppCustomException;
import com.ywrain.appcommon.exception.AppParamsException;
import com.ywrain.appcommon.exception.AppRuntimeException;
import com.ywrain.appcommon.proto.Response;
import com.ywrain.appcommon.utils.ExceptionUtil;
import com.ywrain.appcommon.utils.ReqUtil;
import com.ywrain.appcommon.utils.RespUtil;


/**
 * SpringMVC全局异常处理
 *
 * @author xuguangming@ywrain.com
 */
@ControllerAdvice
public class AppExceptionHandler {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 异常错误处理
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object ExceptionHandler(HttpServletRequest request, Exception e) {
        if (e instanceof AppAuthException) {
            warnLog("登录检查异常", e);
            return Response.errAuth(e.getMessage());
        } else if (e instanceof AppParamsException) {
            warnLog("参数检查异常", e);
            return Response.errParams(e.getMessage());
        } else if (e instanceof AppBizException) {
            warnLog("业务检查异常", e);
            return Response.errBiz(e.getMessage());
        } else if (e instanceof AppRuntimeException) {
            errorLog("应用运行异常", e);
            return Response.errSys();
        } else if (e instanceof AppCustomException) {
            warnLog("自定义异常", e);
            return ((AppCustomException) e).getResponse();
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            errorSimpleLog(ReqConsts.REQ_ERR_MSG, e);
            return Response.errSys(ReqConsts.REQ_ERR_MSG);
        } else {
            errorLog("未捕捉异常", e);
            return RespUtil.getErrSys(ProtoConsts.ERR_MSG_SYS);
        }

    }

    /**
     * 打印警告日志
     * 
     * @param exceptionDesc 异常描述
     * @param ex 异常
     */
    private void warnLog(String exceptionDesc, Exception ex) {
        LOGGER.warn("{}: 消息={}, {}", exceptionDesc, ex.getMessage(), ReqUtil.getShowTrace());
    }

    /**
     * 打印错误日志
     *
     * @param exceptionDesc 异常描述
     * @param ex 异常
     */
    private void errorLog(String exceptionDesc, Exception ex) {
        LOGGER.error("{}: 消息={}, {}, 异常={}", exceptionDesc, ex.getMessage(), ReqUtil.getShowTrace(), ExceptionUtil.getStackTrace(ex));
    }

    /**
     * 打印简单的错误日志, 不打印异常栈
     *
     * @param exceptionDesc 异常描述
     * @param ex 异常
     */
    private void errorSimpleLog(String exceptionDesc, Exception ex) {
        LOGGER.error("{}: 消息={}, {}", exceptionDesc, ex.getMessage(), ReqUtil.getShowTrace());
    }

}
