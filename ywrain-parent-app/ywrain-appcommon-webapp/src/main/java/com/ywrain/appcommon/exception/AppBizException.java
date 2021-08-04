package com.ywrain.appcommon.exception;

/**
 * @description APP业务类处理错误异常
 * @author xuguangming@ywrain.com
 * @date 2017年8月11日
 **/
@SuppressWarnings("serial")
public class AppBizException extends RuntimeException {

    public AppBizException(String msg) {
        super(msg);
    }
}
