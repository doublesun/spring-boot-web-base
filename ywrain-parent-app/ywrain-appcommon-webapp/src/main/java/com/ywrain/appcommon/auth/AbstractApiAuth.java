package com.ywrain.appcommon.auth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ywrain.appcommon.session.DefaultSessionParser;
import com.ywrain.appcommon.session.SessionParser;

public abstract class AbstractApiAuth implements ApiAuth {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected SessionParser sessionParser;

    public AbstractApiAuth() {
        this.sessionParser = new DefaultSessionParser();
    }

    public AbstractApiAuth(SessionParser sessionParser) {
        this.sessionParser = sessionParser;
    }

    public SessionParser getSessionParser() {
        return sessionParser;
    }

    public void setSessionParser(SessionParser sessionParser) {
        this.sessionParser = sessionParser;
    }

    /**
     * 会话失效时间
     */
    protected final Integer SESSION_EXPIRE = 7 * 86400;

    @Override
    public String genSession(String uid) {
        String stoken = createToken(uid);
        // 设置响应头
        if (stoken != null) {
            sessionParser.setSessionToken(stoken);
        } else {
            LOGGER.error("设置用户【{}】登录会话失败：stoken为空", uid);
        }
        return stoken;
    }

    @Override
    public boolean verifySession() {
        String stoken = sessionParser.getSessionToken();
        if (stoken != null) {
            AuthSession authSession = parseToken(stoken);
            return checkValid(authSession);
        }
        return false;
    }

    @Override
    public String getUserId() {
        String stoken = sessionParser.getSessionToken();
        if (stoken != null && !stoken.isEmpty()) {
            AuthSession authSession = parseToken(stoken);
            if (checkValid(authSession))
                return authSession.getUid();
        }
        return null;
    }

    @Override
    public String getUserIdQuietly() {
        String uid = null;
        try {
            uid = getUserId();
        } catch (Exception e) {
            // ignore
        }
        return uid;
    }

    public abstract boolean checkValid(AuthSession authSession);
}
