package com.ywrain.appcommon.session;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ywrain.appcommon.consts.ReqConsts;
import com.ywrain.appcommon.utils.ReqUtil;

/**
 * @author xuguangming@ywrain.com
 * @description 会话TOKEN解析抽象接口
 * @date 2017年12月25日
 **/
public class DefaultSessionParser implements SessionParser {

    private static Set<String> WEB_TYPES = new HashSet<>();
    static {
        // H5移动页
        WEB_TYPES.add("3");
        // 网页
        WEB_TYPES.add("5");
    }

    @Override
    public void setSessionToken(String stoken) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletResponse response = sra.getResponse();
            response.setHeader(ReqConsts.SESSION_TOKEN_NAME, stoken);
            // H5页面额外返回cookie
            String ctype = ReqUtil.getParams("ctype");
            if (WEB_TYPES.contains(ctype)) {
                response.addCookie(new Cookie("stoken", stoken));
            }
        }
    }

    @Override
    public String getSessionToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String stoken = request.getHeader(ReqConsts.SESSION_TOKEN_NAME);
        // 备用方案
        if (stoken == null || stoken.isEmpty()) {
            // H5页面从cookie提取
            String ctype = ReqUtil.getParams("ctype");
            if (WEB_TYPES.contains(ctype)) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (ReqConsts.SESSION_TOKEN_NAME.equalsIgnoreCase(cookie.getName())) {
                        stoken = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return stoken;
    }

}
