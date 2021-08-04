package com.ywrain.common.utils;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import com.ywrain.common.support.StringFormatter;

/**
 * 字符串处理工具类
 *
 * @author weipengfei@youcheyihou.com
 * @since 1.2.0
 */
public class StringUtil extends StringUtils {

    /**
     * 判断字符串数组或列表是否包含null或空白字串对象
     * <pre>
     * StringUtils.isContainEmpty(null, "xxx")      = true
     * StringUtils.isContainEmpty("", "xxx")        = true
     * StringUtils.isContainEmpty(" ", "xxx")       = true
     * StringUtils.isContainEmpty("bob", "xxx")     = false
     * StringUtils.isContainEmpty("  bob  ", "xxx") = false
     * </pre>
     *
     * @param args 字符串数组
     * @return TRUE | FALSE
     */
    public static boolean isContainBlank(String... args) {
        if (args == null) {
            return false;
        }

        for (String arg : args) {
            if (isBlank(arg)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断字符串数组或列表是否包含null或空字串对象
     * <pre>
     * StringUtils.isContainEmpty(null, "xxx")      = true
     * StringUtils.isContainEmpty("", "xxx")        = true
     * StringUtils.isContainEmpty(" ", "xxx")       = false
     * StringUtils.isContainEmpty("bob", "xxx")     = false
     * StringUtils.isContainEmpty("  bob  ", "xxx") = false
     * </pre>
     *
     * @param args 字符串数组
     * @return TRUE | FALSE
     */
    public static boolean isContainEmpty(String... args) {
        if (args == null) {
            return false;
        }

        for (String arg : args) {
            if (isEmpty(arg)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将对象转为字符串，默认UTF8字符集
     * <br> 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
     * <br> 2、对象数组会调用Arrays.toString方法
     * <br> 3、默认调用{@link Object}.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String toStr(final Object obj) {
        return StringFormatter.toStr(obj, StringFormatter.UTF_8);
    }

    /**
     * 将对象转为字符串
     * <br> 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
     * <br> 2、对象数组会调用Arrays.toString方法
     * <br> 3、默认调用{@link Object}.toString方法
     *
     * @param obj 对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String toStr(final Object obj, Charset charset) {
        return StringFormatter.toStr(obj, charset);
    }

    /**
     * 使用指定的格式化处理，将对象转为字符串
     *
     * @param t 对象
     * @param handle 处理实现
     * @return 格式化字符串
     */
    public static <T> String toStr(T t, StringFormatter.FomatterHandle<T> handle) {
        return StringFormatter.toStr(t, handle);
    }

    /**
     * 格式化字符串
     * <br> 此方法只是简单将占位符 {} 按照顺序替换为参数
     * <br> 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可
     * <br> 例：
     * <br> 通常使用：format("this is {} for {}", "a", "b") -> this is a for b
     * <br> 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a
     * <br> 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b
     *
     * @param strTemplate 字符模板
     * @param args 参数对象
     * @return 替换之后的字符串对象
     */
    public static String format(final String strTemplate, final Object... args) {
        return StringFormatter.format(strTemplate, args);
    }

    /**
     * 格式化字符串
     * <br> 此方法只是简单将占位符 {} 按照顺序替换为参数
     * <br> 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可
     * <br> 例：
     * <br> 通常使用：format("this is {} for {}", "a", "b") -> this is a for b
     * <br> 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a
     * <br> 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b
     *
     * @param strTemplate 字符模板
     * @param args 参数对象
     * @return 替换之后的字符串对象
     */
    public static String format(final CharSequence csTemplate, final Object... args) {
        return format(csTemplate.toString(), args);
    }

    /**
     * 格式化缓存KEY的字符串，通过参数替换实际的KEY值，替换的数量由传参决定
     * <br> 性能较差
     *
     * @param tpl 字符模板，定义规范格式(序号从1开始): "/usr/info:id={1}"，"/cars/model:id={1}&type={2}"
     * @param args 参数对象列表，支持java基础类型，默认调用 #.toString() 方法转换为字符类型
     * @return 替换之后的字符串对象
     */
    public static String formatCacheKey(String tpl, Object... args) {
        for (int i = 1; i <= args.length; ++i) {
            if (args[i - 1] != null) {
                tpl = tpl.replace("{" + i + "}", args[i - 1].toString());
            } else {
                tpl = tpl.replace("{" + i + "}", "");
            }
        }
        return tpl;
    }

}
