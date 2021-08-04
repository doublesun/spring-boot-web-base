package com.ywrain.appcommon.exception;

/**
 * @description 参数错误异常
 * @author xuguangming@ywrain.com
 * @date 2017年8月11日
 **/
@SuppressWarnings("serial")
public class AppParamsException extends RuntimeException {

    public AppParamsException(String msg) {
        super(msg);
    }
}
