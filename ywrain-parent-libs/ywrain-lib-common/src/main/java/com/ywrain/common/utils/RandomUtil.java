package com.ywrain.common.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomUtils;

import com.ywrain.common.support.CommonException;

/**
 * 随机数工具类
 * <br> 默认的{@link java.util.Random} 是线程安全的，但多线程访问有锁会影响性能
 * <br> 针对大量线程并发提取随机数的场景，建议使用{@link #getThreadLocalRandom()}
 * <br> 对于随机数
 *
 * @author weipengfei@youcheyihou.com
 * @since 1.2.0
 */
public class RandomUtil extends RandomUtils {

    // 随机数字
    public static final String BASE_NUMBER = "0123456789";
    // 随机数字字符串
    public static final String BASE_CHAR_NUMBER = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 根据权重随机获取指定元素
     *
     * @param maps 带权重值的Map映射表
     * @param <K> 返回的元素类型
     * @return 返回元素值，如果传入列表为空，则返回null
     */
    public static <K> K randomWithWeight(Map<K, Integer> maps) {
        List<Integer> vals = new ArrayList<>(maps.size());
        vals.addAll(maps.values());
        long total = NumberUtil.sum(vals);
        long randVal = nextLong(0, total);
        for (Entry<K, Integer> entry : maps.entrySet()) {
            if (entry.getValue() < 0) {
                throw new IllegalArgumentException(String.format("random weith must be positive number: %d", entry.getValue()));
            }
            if (randVal <= entry.getValue()) {
                return entry.getKey();
            }
            randVal -= entry.getValue();
        }
        // 异常情况
        return null;
    }

    /**
     * 随机获取指定长度的字符串
     * <br> 字符集为26个英文字符+数字
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String randomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException(String.format("random length must be positive number: %d", length));
        }
        final StringBuilder sb = new StringBuilder();
        int len = BASE_CHAR_NUMBER.length();
        for (int i = 0; i < length; i++) {
            int idx = nextInt(0, len);
            sb.append(BASE_CHAR_NUMBER.charAt(idx));
        }
        return sb.toString();
    }

    /**
     * 随机获取指定长度的字符串，仅包含数字
     *
     * @param length 长度>0，否则抛出IllegalArgumentException异常
     * @return 随机字符串
     */
    public static String randomNumberString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException(String.format("random length must be positive number: %d", length));
        }
        final StringBuilder sb = new StringBuilder();
        int len = BASE_NUMBER.length();
        for (int i = 0; i < length; i++) {
            int idx = nextInt(0, len);
            sb.append(BASE_NUMBER.charAt(idx));
        }
        return sb.toString();
    }

    /**
     * 随机获得列表的某个元素
     *
     * @param args 元素列表
     * @param <T> 元素类型
     * @return 随机的元素
     */
    public static <T> T randomList(List<T> args) {
        if (args == null || args.size() <= 0) {
            return null;
        }
        return randomList(args, args.size());
    }

    /**
     * 随机获得列表的某个元素，并限制前limit项
     *
     * @param args 元素列表
     * @param limit 限制前N项，必须大于0
     * @param <T> 元素类型
     * @return 随机的元素
     */
    public static <T> T randomList(List<T> args, int limit) {
        if (args == null || args.size() <= 0 || limit <= 0) {
            return null;
        }
        int max = limit < args.size() ? limit : args.size();
        return args.get(nextInt(0, max));
    }

    /**
     * 随机获得数组元素
     *
     * @param args 数组
     * @param <T> 元素类型
     * @return 随机元素
     */
    public static <T> T randomArray(T... args) {
        if (args == null || args.length <= 0) {
            return null;
        }
        return args[nextInt(0, args.length)];
    }

    /**
     * 获取随机数生成器对象
     * <br> ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程的锁竞争
     *
     * @return {@link ThreadLocalRandom}
     * @since 3.1.2
     */
    public static ThreadLocalRandom getThreadLocalRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 获取{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)
     *
     * @return {@link SecureRandom}
     * @since 3.1.2
     */
    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new CommonException(e);
        }
    }

    /**
     * 获得指定范围内的long型随机数
     *
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     */
    public static long nextIntWithThreadLocal(long min, long max) {
        return getThreadLocalRandom().nextLong(min, max);
    }

    /**
     * 获得指定范围内的int型随机数
     *
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     */
    public static int nextIntWithThreadLocal(int min, int max) {
        return getThreadLocalRandom().nextInt(min, max);
    }

    /**
     * 获得随机数[0, 2^32)
     *
     * @return 随机数
     */
    public static int nextIntWithThreadLocal() {
        return getThreadLocalRandom().nextInt();
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     * @since 3.3.0
     */
    public static double nextDoubleWithThreadLocal(double min, double max) {
        return getThreadLocalRandom().nextDouble(min, max);
    }

    /**
     * 获得随机数[0, 1)
     *
     * @return 随机数
     * @since 3.3.0
     */
    public static double nextDoubleWithThreadLocal() {
        return getThreadLocalRandom().nextDouble();
    }
}
