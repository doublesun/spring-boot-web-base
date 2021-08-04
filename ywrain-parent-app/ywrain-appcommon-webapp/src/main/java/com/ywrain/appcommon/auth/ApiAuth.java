package com.ywrain.appcommon.auth;

public interface ApiAuth {

    /**
     * 根据UID生成并返回会话信息
     *
     * @param uid 用户UserID
     * @return 生成的会话token
     */
    String genSession(String uid);

    /**
     * 校验用户会话身份是否过期，是否合法
     *
     * @return {@code true}表示身份校验合法
     */
    boolean verifySession();

    /**
     * 根据当前会话链接信息，获取用户ID
     *
     * @return 用户UID
     */
    String getUserId();

    /**
     * 安静获取当前会话连接信息中的用户uid
     * <br>兼容空字符处理,解析stoken失败不抛异常
     *
     * @return 用户UID
     */
    String getUserIdQuietly();

    /**
     * 根据用户ID创建token
     * @param uid 用户ID
     * @return 返回token
     */
    String createToken(String uid);

    /**
     * 解析token
     * @param stoken 用户会话token
     * @return 返回AuthSession
     */
    AuthSession parseToken(String stoken);

}
