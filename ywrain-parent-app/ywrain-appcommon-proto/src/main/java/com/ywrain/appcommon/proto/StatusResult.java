package com.ywrain.appcommon.proto;

/**
 * 通用操作状态返回列表
 * 
 * @author xuguangming@ywrain.com
 * @date 2017年8月24日
 */
public class StatusResult {

    public static final int CODE_SUC = 1;

    /**
     * 状态码： 一般而言, 1-成功, 其他具体值的意义由业务层决定
     */
    private Integer status;

    /**
     * 状态消息
     */
    private String msg;

    /**
     * 状态结果附加对象
     */
    private Object info;

    public StatusResult() {}

    /**
     * 
     * @param status 自定义状态码
     */
    public StatusResult(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @param status 自定义状态码
     * @param msg 状态消息
     */
    public StatusResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /**
     * 
     * @param status 自定义状态码
     * @param info 状态结果附加对象
     */
    public StatusResult(Integer status, Object info) {
        this.status = status;
        this.info = info;
    }

    /**
     * 
     * @param status 自定义状态码
     * @param msg 状态消息
     * @param info 状态结果附加对象
     */
    public StatusResult(Integer status, String msg, Object info) {
        this.status = status;
        this.msg = msg;
        this.info = info;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    /* =====  ===== */
    /**
     * 成功结果
     * 
     * @param msg 状态消息
     * @return 状态对象
     */
    public static StatusResult suc(String msg) {
        return build(CODE_SUC, msg);
    }

    /**
     * 成功结果
     * 
     * @param info 状态结果附加对象
     * @return 状态对象
     */
    public static StatusResult suc(Object info) {
        return build(CODE_SUC, null, info);
    }

    /**
     * 成功结果
     * 
     * @param msg 状态消息
     * @param info 状态结果附加对象
     * @return 状态对象
     */
    public static StatusResult suc(String msg, Object info) {
        return build(CODE_SUC, msg, info);
    }

    /**
     * 
     * @param status 自定义状态码
     * @return 状态对象
     */
    public static StatusResult build(Integer status) {
        return new StatusResult(status);
    }

    /**
     * 
     * @param status 自定义状态码
     * @param msg 状态消息
     * @return 状态对象
     */
    public static StatusResult build(Integer status, String msg) {
        return new StatusResult(status, msg);
    }

    /**
     *
     * @param status 自定义状态码
     * @param info 状态结果附加对象
     * @return 状态对象
     */
    public static StatusResult build(Integer status, Object info) {
        return new StatusResult(status, info);
    }

    /**
     * 
     * @param status 自定义状态码
     * @param msg 状态消息
     * @param info 状态结果附加对象
     * @return 状态对象
     */
    public static StatusResult build(Integer status, String msg, Object info) {
        return new StatusResult(status, msg, info);
    }
}
