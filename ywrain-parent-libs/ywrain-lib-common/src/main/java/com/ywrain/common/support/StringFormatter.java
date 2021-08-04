package com.ywrain.common.support;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.ywrain.common.utils.ArrayUtil;
import com.ywrain.common.utils.StringUtil;

/**
 * 字符串格式化
 *
 * @author weipengfei@youcheyihou.com
 * @since 1.2.0
 */
public class StringFormatter {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final String EMPTY_JSON = "{}";
    public static final char C_BACKSLASH = '\\';
    public static final char C_DELIM_START = '{';
    public static final char C_DELIM_END = '}';

    public static interface FomatterHandle<T> {
        String format(T obj);
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
     * @param strTemplate 字符串模板
     * @param args 参数列表
     * @return 结果
     */
    public static String format(final String strTemplate, final Object... args) {
        if (StringUtil.isEmpty(strTemplate) || ArrayUtil.isEmpty(args)) {
            return strTemplate;
        }
        final int strTemplateLen = strTemplate.length();

        // 初始化定义好的长度以获得更好的性能
        StringBuilder sbuf = new StringBuilder(strTemplateLen + 50);

        int handledPosition = 0;
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            delimIndex = strTemplate.indexOf(EMPTY_JSON, handledPosition);
            if (delimIndex == -1) {
                if (handledPosition == 0) {
                    return strTemplate;
                } else { // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                    sbuf.append(strTemplate, handledPosition, strTemplateLen);
                    return sbuf.toString();
                }
            } else {
                if (delimIndex > 0 && strTemplate.charAt(delimIndex - 1) == C_BACKSLASH) {
                    if (delimIndex > 1 && strTemplate.charAt(delimIndex - 2) == C_BACKSLASH) {
                        // 转义符之前还有一个转义符，占位符依旧有效
                        sbuf.append(strTemplate, handledPosition, delimIndex - 1);
                        sbuf.append(toStr(args[argIndex], UTF_8));
                        handledPosition = delimIndex + 2;
                    } else {
                        // 占位符被转义
                        argIndex--;
                        sbuf.append(strTemplate, handledPosition, delimIndex - 1);
                        sbuf.append(C_DELIM_START);
                        handledPosition = delimIndex + 1;
                    }
                } else {
                    // 正常占位符
                    sbuf.append(strTemplate, handledPosition, delimIndex);
                    sbuf.append(toStr(args[argIndex], UTF_8));
                    handledPosition = delimIndex + 2;
                }
            }
        }
        // 加入最后一个占位符后所有的字符
        sbuf.append(strTemplate, handledPosition, strTemplate.length());

        return sbuf.toString();
    }


    /**
     * 将byte数组转为字符串，使用指定的字符集
     * <br> 默认字符集使用UTF8
     *
     * @param data 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String toStr(final byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }
        if (null == charset) {
            return new String(data, UTF_8);
        }
        return new String(data, charset);
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String toStr(final ByteBuffer data, Charset charset) {
        return toStr(data.array(), charset);
    }

    /**
     * 将对象转为字符串
     * <br> 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
     * <br> 2、对象数组会调用ArrayUtil.toString方法
     *
     * @param obj 对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String toStr(final Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return toStr((byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return toStr((ByteBuffer) obj, charset);
        } else if (obj.getClass().isArray()) {
            return ArrayUtil.toString(obj);
        }
        return obj.toString();
    }


    public static <T> String toStr(final T obj, FomatterHandle<T> formatHandle) {
        if (null == obj) {
            return null;
        }
        return formatHandle.format(obj);
    }
}
