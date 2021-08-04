package com.ywrain.appcommon.enums;

/**
 * @description API协议错误码
 * @author xuguangming@ywrain.com
 * @date 2017年12月26日
 **/
public enum ErrCode {

    /**
     * 成功
     */
    NO_ERR(0),
    /**
     * 身份、会话校验失败
     */
    AUTH_ERR(100),
    /**
     * 签名校验失败
     */
    SIGN_ERR(101),
    /**
     * 参数错误
     */
    PARAMS_ERR(102),
    /**
     * 业务错误
     */
    BIZ_ERR(103),
    /**
     * 账户冻结
     */
    FROZENED_ERR(120),
    /**
     * 账户禁言
     */
    FORBIDDEN_ERR(121),
    /**
     * 系统错误
     */
    SYS_ERR(400);

    private int value;

    ErrCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
