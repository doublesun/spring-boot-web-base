package com.ywrain.appcommon.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ywrain.appcommon.exception.AppAuthException;

/**
 * @author xuguangming@ywrain.com
 * @description JWT身份校验实现
 * @date 2017年12月25日
 **/
public class JwtApiAuth extends AbstractApiAuth {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 盐 会话ID生成规则: sid = md5(uid + ets + salt)
     */
    private final String SESSION_SALT = "risk@ywrain.dev";
    /**
     * 会话KEY
     */
    private final String SESSION_KEY = "high_risk@ywrain.com";

    @Override
    public String createToken(String uid) {
        int ets = createEts();
        String sid = createSid(uid, ets);
        String stoken = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SESSION_KEY);
            stoken = JWT.create().withClaim("uid", uid)
                .withClaim("sid", sid)
                .withClaim("ets", ets)
                .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            // UTF-8 encoding not supported
            throw new RuntimeException("create session error: " + e.getMessage());
        } catch (JWTCreationException e) {
            // Invalid Signing configuration / Couldn't convert Claims.
            throw new RuntimeException("create session error：" + e.getMessage());
        }
        return stoken;
    }

    @Override
    public AuthSession parseToken(String stoken) {
        AuthSession jwtSession;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SESSION_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier instance
            DecodedJWT jwt = verifier.verify(stoken);
            jwtSession = new AuthSession();
            jwtSession.setEts(jwt.getClaim("ets").asInt());
            jwtSession.setSid(jwt.getClaim("sid").asString());
            jwtSession.setUid(jwt.getClaim("uid").asString());
        } catch (UnsupportedEncodingException e) {
            // UTF-8 encoding not supported
            throw new AppAuthException("parse session error: " + e.getMessage());
        } catch (JWTVerificationException e) {
            // Invalid signature/claims
            throw new AppAuthException("parse session error: " + e.getMessage());
        }
        return jwtSession;
    }

    @Override
    public boolean checkValid(AuthSession authSession) {
        // TODO: 实现调试白名单

        if (authSession == null) {
            return false;
        }

        Integer ets = authSession.getEts();
        //      // 过期校验
        //      Integer now = Math.round(System.currentTimeMillis() / 1000);
        //      if (ets < now) {
        //          // 超时登录
        //          return false;
        //      }

        // 校验会话ID
        String sid = authSession.getSid();
        if (sid != null) {
            String signSid = createSid(authSession.getUid(), ets);

            return sid.equals(signSid);
        }
        return false;
    }

    protected String createSid(String uid, int ets) {
        StringBuffer sb = new StringBuffer().append(uid).append(ets).append(SESSION_SALT);
        try {
            String sid = MD5(sb.toString());
            return sid;
        } catch (Exception e) {
            throw new RuntimeException("生成会话错误！" + sb.toString());
        }
    }

    protected int createEts() {
        return Math.round(System.currentTimeMillis() / 1000) + SESSION_EXPIRE;
    }

    public static String MD5(String key) {
        char hexDigits[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
