package com.ywrain.appcommon.exception;

/**
 * @description 系统运行异常
 * @author xuguangming@ywrain.com
 * @date 2017年8月11日
 **/
@SuppressWarnings("serial")
public class AppRuntimeException extends RuntimeException {
    
    public AppRuntimeException(String msg) {
        super(msg);
    }
}
