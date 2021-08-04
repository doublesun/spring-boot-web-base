package com.ywrain.appcommon.utils;

import java.util.Set;

/**
 * 参数合法性校验工具类
 *
 * @author xuguangming@ywrain.com
 */
public class ValidateUtil {

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void checkTrue(final boolean expr, String msg) {
        if (!expr) {
            throw ExceptionUtil.getParamsException(msg);
        }
    }

    public static void checkFalse(final boolean expr, String msg) {
        if (expr) {
            throw ExceptionUtil.getParamsException(msg);
        }
    }

    public static void checkNull(final Object obj, String msg) {
        if (obj != null) {
            throw ExceptionUtil.getParamsException(msg);
        }
    }

    public static void checkNotNull(final Object obj, String msg) {
        if (obj == null) {
            throw ExceptionUtil.getParamsException(msg);
        }
    }

    /**
     * 检查字符序列是否非空白字串
     * <pre>
     *     checkBlankAndPattern(null, "msg") -> throw ParamsException msg
     *     checkBlankAndPattern(" ", "msg") -> throw ParamsException msg
     *     checkBlankAndPattern("abcd", "msg") -> ok
     * </pre>
     *
     * @param chars 字符序列
     * @param pattern 正则表达式
     * @param msg 异常消息
     */
    public static <T extends CharSequence> void checkBlank(final T chars, String msg) {
        if (isBlank(chars)) {
            throw ExceptionUtil.getParamsException(msg);
        }
    }

    /**
     * 检查字符序列是否非空白字串，且符合指定的正则表达式
     * <pre>
     *     checkBlankAndPattern(null, "abc+d", "msg") -> throw ParamsException msg
     *     checkBlankAndPattern("abc", "abc+d", "msg") -> throw ParamsException msg
     *     checkBlankAndPattern("abcd", "abc+d", "msg") -> ok
     * </pre>
     *
     * @param chars 字符序列
     * @param pattern 正则表达式
     * @param msg 异常消息
     */
    public static <T extends CharSequence> void checkBlankAndPattern(final T chars, String pattern, String msg) {
        checkBlank(chars, msg);
        String string = chars.toString();
        if (!string.matches(pattern)) {
            throw ExceptionUtil.getParamsException(msg);
        }
    }

    /**
     * 检查字符序列的长度是否满足要求
     *
     * @param chars 字符序列
     * @param maxLength 最大长度
     * @param msg 异常消息
     */
    public static <T extends CharSequence> void checkLength(final T chars, int maxLength, String msg) {
        checkLength(chars, 0, maxLength, msg);
    }

    /**
     * 检查字符序列的长度是否满足要求
     *
     * @param chars 字符序列
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @param msg 异常消息
     */
    public static <T extends CharSequence> void checkLength(final T chars, int minLength, int maxLength, String msg) {
        if (chars == null || chars.length() < minLength || chars.length() > maxLength) {
            throw ExceptionUtil.getParamsException(msg);
        }
    }

    /**
     * 检查对象的是否在集合中
     *
     * @param obj 对象
     * @param rangs 集合范围
     * @param msg 异常消息
     */
    public static <T> void checkIn(final T obj, Set<T> rangs, String msg) {
        if (rangs.size() > 0 && !rangs.contains(obj)) {
            throw ExceptionUtil.getParamsException(msg);
        }
    }
}
