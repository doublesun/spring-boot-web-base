package com.ywrain.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Map操作工具类
 *
 * @author weipengfei@youcheyihou.com
 * @since 1.2.0
 */
public class MapUtil {

    /**
     * Map是否为空
     *
     * @param map MAP
     * @return TRUE | FALSE
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 压缩合并2个List为一个Map
     *
     * @param keys 键列表
     * @param vals 值列表
     * @return Map
     */
    public static <K, V> Map<K, V> zipKeyValList(List<K> keys, List<V> vals) {
        Map<K, V> map = new HashMap<>();
        if (keys != null && vals != null && keys.size() == vals.size()) {
            for (int i = 0; i < keys.size(); i++) {
                V v = vals.get(i);
                if (v != null) {
                    map.putIfAbsent(keys.get(i), vals.get(i));
                }
            }
        }
        return map;
    }

    /**
     * 设置指定的Map的sets value的值
     * <br> 对MAP对象使用同步锁，操作线程安全
     * <br> 如果Sets value 不存在，则创建一个线程安全的 {@link Collections#synchronizedSet(Set)}
     *
     * @param key 键
     * @param value 值
     * @param map 当前指定的Map
     */
    public static <K, V> void setMapSet(K key, V value, Map<K, Set<V>> map) {
        synchronized (map) {
            Set<V> sets = map.get(key);
            if (sets != null) {
                sets.add(value);
            } else {
                sets = Collections.synchronizedSet(new HashSet<V>());
                sets.add(value);
                map.put(key, sets);
            }
        }
    }

    /**
     * 设置指定的Map的sets value的值
     * <br> 对MAP对象使用同步锁，操作线程安全
     *
     * @param key 键
     * @param value 值
     * @param map 当前指定的Map
     */
    public static <K, V> void setMapList(K key, V value, Map<K, List<V>> map) {
        synchronized (map) {
            List<V> list = map.get(key);
            if (list != null) {
                list.add(value);
            } else {
                list = Collections.synchronizedList(new ArrayList<>());
                list.add(value);
                map.put(key, list);
            }
        }
    }

    /**
     * 将单一键值对，转换生成HashMap
     *
     * @param key 键
     * @param value 值
     * @param <K> 泛型K
     * @param <V> 泛型V
     * @return Map
     */
    public static <K, V> HashMap<K, V> ofHashMap(K key, V value) {
        final HashMap<K, V> map = new HashMap<K, V>(2);
        map.put(key, value);
        return map;
    }

    /**
     * 将单一键值对，转换生成有序的LinkedHashMap
     *
     * @param key 键
     * @param value 值
     * @param <K> 泛型K
     * @param <V> 泛型V
     * @return 有序的LinkedHashMap
     */
    public static <K, V> HashMap<K, V> ofLinkedHashMap(K key, V value) {
        final HashMap<K, V> map = new LinkedHashMap<K, V>(2);
        map.put(key, value);
        return map;
    }

    /**
     * 获取值对象，如果Map为空，返回null
     *
     * @param key 键
     * @param map 指定的Map
     * @param <K> 泛型K
     * @param <V> 泛型V
     * @return 值
     */
    public static <K, V> V getObject(final Map<? super K, V> map, final K key) {
        if (map != null) {
            return map.get(key);
        }
        return null;
    }
}
