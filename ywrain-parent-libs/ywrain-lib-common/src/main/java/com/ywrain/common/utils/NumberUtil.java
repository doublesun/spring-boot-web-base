package com.ywrain.common.utils;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 数值处理工具类
 * 
 * @since 版本:1.2.0
 * @author caixiwei@youcheyihou.com
 */
public class NumberUtil extends NumberUtils {

    /**
     * 汇总整形值列表数值
     *
     * @param list 列表
     * @return 汇总值
     */
    public static long sum(List<Integer> list) {
        long sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        return sum;
    }

    /**
     * 是否为正确的ID值: ID不为空,且ID值不允许小于1
     * 
     * @param id ID对象
     * @return 是否为正确的ID值:true-是,false-否
     */
    public static boolean isCorrectId(Integer id) {
        if (id == null) {
            return false;
        } else if (id < 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否为错误的ID值: ID为空或者ID值小于1
     * 
     * @param id ID对象
     * @return 是否为错误的ID值:true-是,false-否
     */
    public static boolean isErrorId(Integer id) {
        return !isCorrectId(id);
    }

    /**
     * 是否为正确的ID值: ID不为空,且ID值不允许小于1
     * 
     * @param id ID对象
     * @return 是否为正确的ID值:true-是,false-否
     */
    public static boolean isCorrectId(Long id) {
        if (id == null) {
            return false;
        } else if (id < 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否为错误的ID值: ID为空或者ID值小于1
     * 
     * @param id ID对象
     * @return 是否为错误的ID值:true-是,false-否
     */
    public static boolean isErrorId(Long id) {
        return !isCorrectId(id);
    }

    /**
     * 两个整形值比较<br>
     * http://www.cnblogs.com/xh0102/p/5280032.html<br>
     *
     * <pre>
     *  NumberUtil.equals(null, null) = true
     *  NumberUtil.equals(null, 1) = false
     *  NumberUtil.equals(1, 2) = false
     *  NumberUtil.equals(129, 129) = true
     * </pre>
     * 
     * @param a 对比值1
     * @param b 对比值2
     * @return 是否相等
     */
    public static boolean equals(Integer a, Integer b) {
        boolean flag = false;
        if (a == null || b == null) {
            if (a == null && b == null) {
                flag = true;
            }
        } else {
            flag = (a.intValue() == b.intValue());
        }
        return flag;
    }

    /**
     * 两个整形值比较<br>
     * http://www.cnblogs.com/xh0102/p/5280032.html<br>
     * 
     * <pre>
     *  NumberUtil.equals(null, null, 1) = true
     *  NumberUtil.equals(1, null, 1) = false
     *  NumberUtil.equals(1, 2,3) = false
     *  NumberUtil.equals(129, 129, 130) = true
     * </pre>
     * 
     * @param num 对比值num
     * @param searchNums 匹配值列表
     * @return 匹配值列表
     */
    public static boolean equalsAny(final Integer num, final Integer... searchNums) {
        if (ArrayUtils.isNotEmpty(searchNums)) {
            for (final Integer next : searchNums) {
                if (equals(num, next)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 两个长整形值比较<br>
     *
     * <pre>
     *  NumberUtil.equals(null, null) = true
     *  NumberUtil.equals(null, 1L) = false
     *  NumberUtil.equals(1L, 2L) = false
     *  NumberUtil.equals(129L, 129L) = true
     * </pre>
     *
     * @param a 对比值1
     * @param b 对比值2
     * @return 是否相等
     */
    public static boolean equals(Long a, Long b) {
        boolean flag = false;
        if (a == null || b == null) {
            if (a == null && b == null) {
                flag = true;
            }
        } else {
            flag = (a.longValue() == b.longValue());
        }
        return flag;
    }

    /**
     * 两个长整形值比较<br>
     * 
     * <pre>
     *  NumberUtil.equals(null, null, 1L) = true
     *  NumberUtil.equals(1L, 2L, 3L) = false
     *  NumberUtil.equals(129L, 129L, 130L) = true
     * </pre>
     * 
     * @param num 对比值num
     * @param searchNums 匹配值列表
     * @return 是否相等
     */
    public static boolean equalsAny(final Long num, final Long... searchNums) {
        if (ArrayUtils.isNotEmpty(searchNums)) {
            for (final Long next : searchNums) {
                if (equals(num, next)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * num是否在min和max之间(包含关系)<br>
     *
     * <pre>
     *  NumberUtil.isRang(null, 1, 3) = false
     *  NumberUtil.isRang(2, null, 3) = false
     *  NumberUtil.isRang(2, 1, null) = false
     *  NumberUtil.isRang(1, 1, 3) = true
     *  NumberUtil.isRang(2, 1, 3) = true
     *  NumberUtil.isRang(3, 1, 3) = true
     *  NumberUtil.isRang(0, 1, 3) = false
     *  NumberUtil.isRang(4, 1, 3) = false
     * </pre>
     *
     * @param num 数值
     * @param min 最小值
     * @param max 最大值
     * @return 是否在范围内:true-是,false-否
     */
    public static boolean isRang(Integer num, Integer min, Integer max) {
        boolean _val = false;
        if (min != null && max != null && num != null) {
            if (num >= min && num <= max) {
                _val = true;
            }
        }
        return _val;
    }
}
