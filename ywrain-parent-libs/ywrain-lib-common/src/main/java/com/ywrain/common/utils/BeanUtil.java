package com.ywrain.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ywrain.common.support.AsmReflectAccess;
import com.ywrain.common.support.AsmReflectWrapper;
import com.ywrain.common.support.BeanCopierWrapper;
import com.ywrain.common.support.CommonException;

/**
 * Bean拷贝等相关操作工具类
 * <br> 注意: 建议项目针对待copy的类进行拷贝代理的初始化调用{@link #initCopier(Class, Class)}
 * <pre>
 *     拷贝类型：对象深拷贝，内部成员变量浅拷贝
 *
 *     简单测试性能（循环100000）:
 *     [PerfTest]BeanUtil.init: 157
 *     [PerfTest]BeanUtil.copyFast:  : 23
 *     [PerfTest]BeanUtil.copyFast:  : 26
 *     [PerfTest]BeanUtil.copy: 66
 *     [PerfTest]JsonUtil.copy: 1482
 *     [PerfTest]JsonUtil.toJsonString: 318
 * </pre>
 * <pre>
 *     参考 <a href="https://github.com/neoremind/easy-mapper">easy-mapper</a>
 * </pre>
 *
 * @author weipengfei@youcheyihou.com
 * @since 1.2.0
 */
public class BeanUtil {

    private static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    public static void initCopier(Class<?> src, Class<?> target) {
        BeanCopierWrapper.create(src, target);

        AsmReflectWrapper.parseClassAccess(src);
        AsmReflectWrapper.parseClassAccess(target);
    }

    /**
     * 快速复制源和目标对象间的同名同类型属性变量字段
     * <br> 使用CGlib BeanCopier 库
     * <br> 仅拷贝成员变量字段，需要保证getter/setter方法对应
     *
     * @param src 来源对象
     * @param target 目标对象
     */
    public static void copyFast(Object src, Object target) {
        BeanCopierWrapper.copy(src, target);
    }

    /**
     * 快速复制源对象的同名同类型属性变量字段，目标对象根据指定类型新建
     * <br> 使用CGlib BeanCopier 库
     * <br> 目标对象类必须是公共类，或内部静态类；内部私有类无法新建对象
     * <br> 仅拷贝成员变量字段，需要保证getter/setter方法对应
     *
     * @param src 来源对象
     * @param tClazz 目标对象类
     * @param <T> 目标对象类
     * @return 新目标对象
     */
    public static <T> T copyFast(Object src, Class<T> tClazz) {
        T target = null;
        try {
            target = tClazz.newInstance();

            copyFast(src, target);
        } catch (Exception e) {
            throw new CommonException("[BeanUtil]copy bean error: ", e);
        }
        return target;
    }

    /**
     * 复制源和目标对象间的同名同类型属性变量字段，包括public字段
     * <br> 使用{@link com.ywrain.common.support.AsmReflectWrapper} 提取反射代理处理
     *
     * @param src 来源对象
     * @param target 目标对象
     */
    public static void copy(Object src, Object target) {
        copy(src, target, null);
    }

    /**
     * 复制源和目标对象间的指定的同名同类型属性变量字段，包括public字段
     * <br> 使用{@link com.ywrain.common.support.AsmReflectWrapper} 提取反射代理处理
     * <br> 仅复制指定的字段名的成员变量，包括public的
     *
     * @param src 源对象
     * @param target 目标对象
     * @param limitFields 指定的字段，需要保证目标和源都有getter或setter方法
     */
    public static void copy(Object src, Object target, Set<String> limitFields) {
        AsmReflectAccess accessSrc = AsmReflectWrapper.parseClassAccess(src.getClass());
        AsmReflectAccess accessTarget = AsmReflectWrapper.parseClassAccess(target.getClass());
        Set<String> sets;
        // 复制public成员变量
        if (limitFields != null && limitFields.size() > 0) {
            sets = limitFields;
        } else {
            sets = accessTarget.getMapPubFields().keySet();
        }
        for (String f : sets) {
            Integer idxTarget = accessTarget.getMapPubFields().get(f);
            if (null != idxTarget) {
                Integer idxSrc = accessSrc.getMapPubFields().get(f);
                if (null != idxSrc) {
                    Object v = accessSrc.getFieldAccess().get(src, idxSrc);
                    if (null != v) {
                        accessTarget.getFieldAccess().set(target, accessTarget.getMapPubFields().get(f), v);
                    }
                }
            }
        }
        // 复制其他成员变量
        if (limitFields != null && limitFields.size() > 0) {
            sets = limitFields;
        } else {
            sets = accessTarget.getMapSetMethods().keySet();
        }
        for (String f : sets) {
            Integer idxTarget = accessTarget.getMapSetMethods().get(f);
            if (null != idxTarget) {
                Integer idxSrc = accessSrc.getMapGetMethods().get(f);
                if (null != idxSrc) {
                    Object v = accessSrc.getMethodAccess().invoke(src, idxSrc.intValue());
                    if (v != null) {
                        // null值不拷贝，保持target的默认值
                        accessTarget.getMethodAccess().invoke(target, idxTarget.intValue(), v);
                    }
                }
            }
        }
    }

    /**
     * 复制源和目标对象间的同名同类型属性变量字段，包括public字段
     * <br> 使用{@link com.ywrain.common.support.AsmReflectWrapper} 提取反射代理处理
     *
     * @param src 来源对象
     * @param tClazz 目标对象类
     * @param <T> 目标对象类
     * @return 新目标对象
     */
    public static <T> T copy(Object src, Class<T> tClazz) {
        return copy(src, tClazz, null);
    }


    /**
     * 复制源对象，生成新的目标对象
     *
     * @param src 源对象列表
     * @param tClazz 目标对象类
     * @param <S> 源类型
     * @param <T> 目标类型
     * @param limitFields 限制字段列表
     * @return 新目标对象
     */
    public static <T> T copy(Object src, Class<T> tClazz, Set<String> limitFields) {
        T target = null;
        try {
            target = tClazz.newInstance();

            copy(src, target, limitFields);
        } catch (Exception e) {
            throw new CommonException("[BeanUtil]copy bean error: ", e);
        }
        return target;
    }

    /**
     * 批量复制一组源对象，生成新的目标对象list
     *
     * @param srcList 源对象列表
     * @param tClazz 目标对象类
     * @param <S> 源类型
     * @param <T> 目标类型
     * @return 目标对象list
     */
    public static <S, T> List<T> copyList(List<S> srcList, Class<?> tClazz) {
        List<T> targetList = new ArrayList<>();
        if (srcList != null && srcList.size() > 0) {
            try {
                for (Object o : srcList) {
                    T t = (T) tClazz.newInstance();
                    copy(o, t);
                    targetList.add(t);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CommonException("[BeanUtil]copy bean list error: ", e);
            }
        }
        return targetList;
    }


    /**
     * 批量复制一组源对象，生成新的目标对象list
     *
     * @param srcList 源对象列表
     * @param tClazz 目标对象类
     * @param <S> 源类型
     * @param <T> 目标类型
     * @param limitFields 限制字段列表
     * @return 目标对象list
     */
    public static <S, T> List<T> copyListLimit(List<S> srcList, Class<?> tClazz, Set<String> limitFields) {
        List<T> targetList = new ArrayList<>();
        if (srcList != null && srcList.size() > 0) {
            try {
                for (Object o : srcList) {
                    T t = (T) tClazz.newInstance();
                    copy(o, t, limitFields);
                    targetList.add(t);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CommonException("[BeanUtil]copy bean list error: ", e);
            }
        }
        return targetList;
    }
}
