package com.ywrain.common.support;

/**
 * 通用包的运行时异常
 *
 * @since 1.2.0
 * @author weipengfei@youcheyihou.com
 */
public class CommonException extends RuntimeException {

    public CommonException(String msg) {
        super(msg);
    }

    public CommonException(Throwable e) {
        super(e);
    }

    public CommonException(String msg, Throwable e) {
        super(e);
    }
}
