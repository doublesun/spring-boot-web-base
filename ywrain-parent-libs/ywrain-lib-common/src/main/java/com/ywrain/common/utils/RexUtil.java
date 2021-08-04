package com.ywrain.common.utils;

import java.util.regex.Pattern;

/**
 * 正则匹配工具方法集合
 * <a href=https://gitee.com/loolly/hutool/blob/v4-master/hutool-core/src/main/java/cn/hutool/core/util/ReUtil.java>参考</a>
 *
 * @author weipengfei@youcheyihou.com
 */
public class RexUtil {

    /**
     * 给定内容是否匹配正则
     *
     * @param regex 正则
     * @param content 内容
     * @return TRUE | FALSE 正则为null或者""则不检查，返回true，内容为null返回false
     */
    public static boolean isMatch(String regex, CharSequence content) {
        if (content == null) {
            // 提供null的字符串为不匹配
            return false;
        }

        if (StringUtil.isEmpty(regex)) {
            // 正则不存在则为全匹配
            return true;
        }

        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
//        final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
        return isMatch(pattern, content);
    }

    /**
     * 给定内容是否匹配正则
     *
     * @param pattern 模式
     * @param content 内容
     * @return TRUE | FALSE 正则为null或者内容为null返回false
     */
    public static boolean isMatch(Pattern pattern, CharSequence content) {
        if (content == null || pattern == null) {
            // 提供null的字符串为不匹配
            return false;
        }
        return pattern.matcher(content).matches();
    }
}
