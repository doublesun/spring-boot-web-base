package com.ywrain.appcommon.auth;

/**
 * 用户会话载体，称为用户会话TOKEN
 *
 * @author xuguangming@ywrain.com
 * @date 2017年3月22日
 */
public class AuthSession {
    /**
     * 用户UID 未注册登录用户，默认"visitor"
     */
    private String uid;

    /**
     * 用户会话标识ID 未注册登录用户，默认传空字符串
     */
    private String sid;

    /**
     * 本次会话过期时间戳unixtime，默认不过期
     * <br>会话过期仅对WEB浏览器限制有效，过期后要求重新登录
     * <br>移动端会话过期后校验AuthSession.sid合法后可重发token值，或者返回校验失败时，由客户端隐式调用接口重新登录身份
     */
    private Integer ets = 0;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getEts() {
        return ets;
    }

    public void setEts(Integer ets) {
        this.ets = ets;
    }

}
