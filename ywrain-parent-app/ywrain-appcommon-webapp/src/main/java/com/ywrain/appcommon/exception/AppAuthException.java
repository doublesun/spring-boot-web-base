package com.ywrain.appcommon.exception;

/**
 * @description 身份鉴权异常
 * @author xuguangming@ywrain.com
 * @date 2017年12月26日
 **/
@SuppressWarnings("serial")
public class AppAuthException extends RuntimeException {
    
    public AppAuthException(String msg) {
        super(msg);
    }
}
