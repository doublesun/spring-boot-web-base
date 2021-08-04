package com.ywrain.common.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.beans.BeanCopier;

/**
 * Cglib的BeanCopier的包装代理
 * <pre>
 *     经常规测试，缓存BeanCopier对性能提升影响并不明显
 * </pre>
 *
 * @author weipengfei@youcheyihou.com
 */
public class BeanCopierWrapper {

    // 缓存
    private static final Map<String, BeanCopier> MAP_BEAN_COPIERs = new ConcurrentHashMap<>();

    /**
     * 复制属性字段
     *
     * @param sObj 源对象
     * @param tObj 目标对象
     */
    public static void copy(Object sObj, Object tObj) {
        Class<?> src = sObj.getClass();
        Class<?> target = tObj.getClass();
        BeanCopier beanCopier = create(src, target);
        beanCopier.copy(sObj, tObj, null);
    }

    /**
     * 创建2个对象类型间的BeanCopier映射
     *
     * @param target 目标对象
     * @param src 来源对象
     */
    public static BeanCopier create(Class<?> src, Class<?> target) {
        String key = generateKey(src, target);
        return MAP_BEAN_COPIERs.computeIfAbsent(key, k -> BeanCopier.create(src, target, false));
//        return BeanCopier.create(src, target, false);
    }

    private static String generateKey(Class<?> source, Class<?> target) {
        StringBuilder sb = new StringBuilder();
        sb.append(source.getName()).append(target.getName());
        return sb.toString();
    }
}
