package com.ywrain.appcommon.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.ywrain.appcommon.consts.ReqConsts;
import com.ywrain.appcommon.proto.ClientExt;
import com.ywrain.appcommon.proto.Response;

/**
 * 提供Request的相关参数提取，通用判定等工具类方法
 *
 * @author xuguangming@ywrain.com
 * @date 2017年8月24日
 **/
public class ReqUtil {
    public static Logger LOGGER = LoggerFactory.getLogger(ReqUtil.class);

    public final static List<String> apiOldUriPatterns = new ArrayList<>();
    public final static List<String> apiUriPatterns = new ArrayList<>();
    public final static List<String> rpcUriPatterns = new ArrayList<>();

    static {
        // -----------------------
        // 新旧协议
        // -----------------------
        // 车型库
        apiUriPatterns.add("/cardata/**");
        // 包括新旧协议
        apiUriPatterns.add("/ywrain_*/**");

        // -----------------------
        // 旧协议
        // -----------------------
        apiOldUriPatterns.add("/ywrain*/common");
        apiOldUriPatterns.add("/QA_platform/common**");
        apiOldUriPatterns.add("/experiences_scores/common**");

        // -----------------------
        // REST-RPC协议
        // -----------------------
        rpcUriPatterns.add("/rpc/**");
    }

    /**
     * 判断字符串是否为空
     * 
     * <pre>
     *     null -> true
     *     " " -> true
     *     "ssd " -> false
     * </pre>
     *
     * @param cs 字节串
     * @return true | false
     */
    public static boolean isStringBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为整型值
     * 
     * <pre>
     *     结果示例：
     *     null -> false
     *     " " -> false
     *     "123" -> true
     *
     *     注意：该方法效率不高，请优先使用org.apache.commons.lang3.math.NumberUtils.isCreatable/2
     * </pre>
     *
     * @param cs 字节串
     * @return true | false
     */
    public static boolean isNumber(CharSequence s) {
        if (s == null || s.length() <= 0) {
            return false;
        }

        for (int i = s.length(); --i >= 0;) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 当前请求是否是旧API接口
     *
     * @return true | false
     */
    public static boolean isOldApiUri() {
        String uri = getUri();
        if (matchUriPattern(uri, apiOldUriPatterns)) {
            return true;
        }
        return false;
    }

    /**
     * 当前请求是否API接口
     *
     * @return true | false
     */
    public static boolean isApiUri() {
        String uri = getUri();
        if (matchUriPattern(uri, apiUriPatterns)) {
            return true;
        }
        return false;
    }

    /**
     * 当前请求是否RPC接口
     *
     * @return true | false
     */
    public static boolean isRpcUri() {
        String uri = getUri();
        if (matchUriPattern(uri, rpcUriPatterns)) {
            return true;
        }
        return false;
    }

    /**
     * 检测是否是Ajax请求
     *
     * @return true | false
     */
    public static boolean isAjax(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if (requestType != null && requestType.equals("XMLHttpRequest")) {
            return true;
        }
        return false;
    }

    /**
     * 提取ClientIP
     *
     * @return ClientIp 192.168.0.10
     */
    public static String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // String ip = req.getRemoteAddr();
        // 优先提取x-real-ip（在现有有车以后的架构下，优先该值代表client ip，通过代理网关透传）
        String ip = request.getHeader("x-real-ip");
        if (ip == null || "unknown".equalsIgnoreCase(ip.trim()) || ip.trim().isEmpty()) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip.trim()) || ip.trim().isEmpty()) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip.trim()) || ip.trim().isEmpty()) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip.trim()) || ip.trim().isEmpty()) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    /**
     * 提取完整的客户端扩展信息
     *
     * @return ClientExt
     */
    public static ClientExt getClientExt() {
        String ext = getParams("ext");
        if (!isStringBlank(ext)) {
            return new Gson().fromJson(ext, ClientExt.class);
        }
        return null;
    }

    /**
     * 提取ctype字段，如：4
     *
     * @return ctype
     */
    public static int getCtype() {
        String s = getParams("ctype");
        if (!isNumber(s)) {
            throw ExceptionUtil.getParamsException("ctype字段未找到或不正确");
        }
        return Integer.parseInt(s);
    }

    /**
     * 提取cname字段，如："APP_SUV"
     *
     * @return cname
     */
    public static String getCname() {
        String s = getParams("cname");
        if (s == null) {
            throw ExceptionUtil.getParamsException("cname字段未找到");
        }
        return s;
    }

    /**
     * 提取c_ver字段值，整型值，如：302000
     *
     * @return cver
     */
    public static int getCver() {
        String s = getParams("c_ver");
        if (!isNumber(s)) {
            s = getParams("cver");
            if (!isNumber(s)) {
                throw ExceptionUtil.getParamsException("c_ver字段未找到或不正确");
            }
        }
        return Integer.parseInt(s);
    }

    /**
     * 提取参数，未找到时返回空字符串
     *
     * @return null或具体的字符串对象
     */
    public static String getParams(String key) {
        HttpServletRequest request = getRequest();
        String val = request.getParameter(key);
        return val;
    }

    /**
     * 提取所有访问请求参数，返回MAP，相同的参数，返回第一个参数值
     *
     * @return String
     */
    public static Map<String, String> getParams() {
        Map<String, String> _params = new HashMap<>();
        HttpServletRequest request = getRequest();
        Map<String, String[]> params = request.getParameterMap();
        if (params != null) {
            for (String key : params.keySet()) {
                String value = null;
                String[] values = params.get(key);
                if (values != null && values.length > 0) {
                    value = values[0];
                }
                _params.put(key, value);
            }
        }
        return _params;
    }


    /**
     * 请求参数转string
     *
     * @return String 格式:"id=xxx&name=xx" 或 ""
     */
    public static String paramsToString() {
        List<String> queryStrings = new ArrayList<>();
        Map<String, String[]> params = ReqUtil.getRequest().getParameterMap();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                queryStrings.add(key + "=" + value);
            }
        }
        if (queryStrings.isEmpty()) {
            return "";
        } else {
            return String.join("&", queryStrings);
        }
    }

    /**
     * 提取URI
     *
     * @return String
     */
    public static String getUri() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return req.getRequestURI();
    }

    /**
     * 提取显示用的trace信息
     */
    public static String getShowTrace() {
        StringBuilder sb = new StringBuilder();
        String method = ReqUtil.getRequest().getMethod();
        String url = ReqUtil.getRequest().getRequestURL().toString();
        String queryStr = paramsToString();
        sb.append("请求地址:").append(method).append(" ").append(url);
        sb.append(", 请求参数:").append(queryStr);
        sb.append(", 客户端IP:").append(getClientIp());
        sb.append(", stoken:").append(getRequest().getHeader("stoken"));
        sb.append(", traceid:").append((String) getRequest().getAttribute(ReqConsts.REQ_FLAG_TRACE_KEY));
        return sb.toString();
    }

    /**
     * 提取Request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return req;
    }

    /**
     * 提取Respose
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    /**
     * 设置servlet线程请求属性
     *
     * @param key 键
     * @param value 值
     */
    public static <T> void setAttr(String key, T value) {
        HttpServletRequest req = getRequest();
        req.setAttribute(key, value);
    }

    /**
     * 提取请求的预设属性
     *
     * @param key 键
     * @param <T> 强转类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttr(String key) {
        HttpServletRequest req = getRequest();
        return (T) req.getAttribute(key);
    }

    /**
     * 关闭链接，返回指定错误
     */
    public static void close(HttpServletResponse response, int errCode, String msg) throws IOException {
        try {
            ServletOutputStream out = response.getOutputStream();
            Response resp = RespUtil.getErr(errCode, msg);
            out.write(resp.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 匹配uri的模式
     *
     * @return Boolean
     */
    public static boolean matchUriPattern(String uri, List<String> listExcludePatterns) {
        PathMatcher matcher = new AntPathMatcher();
        boolean result = false;
        for (String p : listExcludePatterns) {
            result = matcher.match(p, uri);
            if (result) {
                break;
            }
        }
        return result;
    }
}
