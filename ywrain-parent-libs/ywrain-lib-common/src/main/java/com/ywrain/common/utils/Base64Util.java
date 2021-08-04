package com.ywrain.common.utils;

import java.util.Base64;

import com.ywrain.common.support.StringFormatter;

/**
 * Base64的处理工具类
 * <br> 注意：尚未测试MIME编码---使用基本的字母数字产生BASE64输出，而且对MIME格式友好：每一行输出不超过76个字符，而且每行以“\r\n”符结束。
 *
 * @author weipengfei@youcheyihou.com
 */
public class Base64Util {

    /**
     * 加密Base64字符
     *
     * @param src 源字符
     * @return 返回base64字符串
     */
    public static String encode(String src) {
        return encode(src.getBytes(StringFormatter.UTF_8));
    }

    public static String encode(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    /**
     * 解密Base64字符
     *
     * @param src base64字符串
     * @return 返回源字符
     */
    public static String decode(String src) {
        return new String(Base64.getDecoder().decode(src), StringFormatter.UTF_8);
    }

    /**
     * 加密Base64 URL字符
     *
     * @param str 源字符
     * @return 返回base64字符串
     */
    public static String encodeUrl(String src) {
        return encodeUrl(src.getBytes(StringFormatter.UTF_8));
    }

    public static String encodeUrl(byte[] src) {
        return Base64.getUrlEncoder().encodeToString(src);
    }

    /**
     * 解密Base64 URL字符
     *
     * @param src base64字符串
     * @return 返回源字符
     */
    public static String decodeUrl(String src) {
        return new String(Base64.getUrlDecoder().decode(src), StringFormatter.UTF_8);
    }

}
