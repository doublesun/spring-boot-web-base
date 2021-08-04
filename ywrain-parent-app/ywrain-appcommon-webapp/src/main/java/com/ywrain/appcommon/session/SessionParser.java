package com.ywrain.appcommon.session;

/**
 * @description 会话TOKEN解析抽象接口
 * @author xuguangming@ywrain.com
 * @date 2017年12月25日
 **/
public interface SessionParser {
    /**
     * 生成TOKEN
     * @param uid
     * @return
     */
    public void setSessionToken(String stoken);
    /**
     * 解析TOKEN
     * @return
     */
    public String getSessionToken();
}
