package com.ywrain.common.utils;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ywrain.common.support.AsmReflectAccess;
import com.ywrain.common.support.AsmReflectWrapper;
import com.ywrain.common.support.CommonException;

/**
 * Obejct 对象操作工具类 create in 2019/6/15
 *
 * @author weipengfei@youcheyihou.com
 */
public class ObjectUtil {

    private static Object parseValue(Field field, String value) {
        if (StringUtil.isBlank(value)) {
            return null;
        }
        String typeName = field.getType().getName();
        switch (typeName) {
            case "java.lang.String":
                return value;
            case "java.lang.Number":
                try {
                    return NumberFormat.getInstance().parse(value);
                } catch (ParseException e) {
                    throw new CommonException("fill failure", e);
                }
            case "int":
                return Integer.parseInt(value);
            case "java.lang.Integer":
                return Integer.valueOf(value);
            case "double":
                return Double.parseDouble(value);
            case "java.lang.Double":
                return Double.valueOf(value);
            case "long":
                return Long.parseLong(value);
            case "java.lang.Long":
                return Long.valueOf(value);
            case "boolean":
                return Boolean.parseBoolean(value);
            case "java.lang.Boolean":
                return Boolean.valueOf(value);
            case "java.util.Date":
                if (value.trim().length() > 0) {
                    // 当前仅当日期字符串不为空
                    try {
                        // 用带毫秒格式化类转换
                        Date date = DateUtil.parseToDatetime(DateUtil.DefaultDateTimeFormatSssPattern, value);
                        return date;
                    } catch (Exception e1) {
                        try {
                            // 用不带毫秒日期格式化类转换
                            Date date = DateUtil.parseToDatetime(DateUtil.DefaultDateTimeFormat, value);
                            return date;
                        } catch (Exception e2) {
                            throw new CommonException(StringUtil.format("map值设置到bean({}), value={}, 异常={}", typeName, value, e2.getMessage()));
                        }
                    }
                }
            default:
                if (!field.getType().isArray() && !field.getType().isInterface()) {
                    return JsonUtil.toObject(value, field.getType());
                }
                break;
        }
        return null;
    }

    private static String packValueToString(Field field, Object value) {
        if (null == value) {
            return null;
        }
        switch (field.getType().getName()) {
            case "java.lang.String":
            case "java.lang.Number":
            case "int":
            case "java.lang.Integer":
            case "long":
            case "java.lang.Long":
            case "float":
            case "java.lang.Float":
            case "double":
            case "java.lang.Double":
            case "boolean":
            case "java.lang.Boolean":
                return value.toString();
            case "java.util.Date":
                return DateUtil.formatToString(DateUtil.DefaultDateTimeSssFormat, (Date) value);
            default:
                if (!field.getType().isArray() && !field.getType().isInterface()) {
                    return JsonUtil.toJsonString(value);
                }
                break;
        }
        return null;
    }

    /**
     * 将Map表映射数据，反序列化到指定的目标对象
     * <pre>
     *     注意：
     *     1、支持字段是Java基础类型的对象字段赋值，和其他对象类字段，反序列化使用{@link JsonUtil#toObject(String, Class)}
     *     2、支持public/private字段
     * </pre>
     *
     * @param maps 映射表
     * @param tClazz 目标类
     * @return 目标对象
     */
    public static <T> T fillToFields(Map<String, String> maps, Class<T> tClazz)
        throws IllegalAccessException, InstantiationException, ParseException {
        T target = tClazz.newInstance();
        fillToFields(maps, target);
        return target;
    }

    public static <T> void fillToFields(Map<String, String> maps, T targetObject) {
        Class<?> superClazz = targetObject.getClass();
        AsmReflectAccess accessTarget = AsmReflectWrapper.parseClassAccess(superClazz);
        while (superClazz != null) {
            Field[] fields = superClazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().isLocalClass()) {
                    // 内部类，不支持，抛异常
                    throw new CommonException("cant be local class");
                }
                String value = maps.get(field.getName());
                Object v = parseValue(field, value);
                if (v != null) {
                    Integer idx = accessTarget.getMapSetMethods().get(field.getName());
                    if (idx != null) {
                        accessTarget.getMethodAccess().invoke(targetObject, idx, v);
                    } else {
                        // 尝试public成员
                        Integer idxPublic = accessTarget.getMapPubFields().get(field.getName());
                        if (idxPublic != null) {
                            accessTarget.getFieldAccess().set(targetObject, idxPublic, v);
                        }
                    }
                }
            }
            superClazz = superClazz.getSuperclass();
        }
    }

    /**
     * 将对象的各个字段按名称解析为Map映射
     * <br> 仅包括public 或 private私有成员变量
     * <br> Java基础类型字段值序列化使用{@link Object#toString()}
     * <br> 其他对象类字段序列化使用{@link JsonUtil#toObject(String, Class)}
     *
     * @param src 对象
     * @return Map映射
     */
    public static Map<String, String> parseToStringMap(Object src) {
        Map<String, String> mapFieldVals = new HashMap<>();
        AsmReflectAccess accessSrc = AsmReflectWrapper.parseClassAccess(src.getClass());

        accessSrc.getMapAllFields().forEach((k, v) -> {
            Integer idx = accessSrc.getMapPubFields().get(k);
            if (idx != null) {
                // 遍历public字段
                Object t = accessSrc.getFieldAccess().get(src, idx);
                if (t != null) {
                    mapFieldVals.putIfAbsent(k, packValueToString(v, t));
                }
            } else {
                // 遍历private变量
                idx = accessSrc.getMapGetMethods().get(k);
                Object t = idx != null ? accessSrc.getMethodAccess().invoke(src, idx) : null;
                if (t != null) {
                    mapFieldVals.putIfAbsent(k, packValueToString(v, t));
                }
            }
        });
        return mapFieldVals;
    }


    /**
     * 将对象的各个字段按名称解析为Map映射，且将值转为序列化为字符串
     * <br> 支持public 或 private私有成员变量
     *
     * @param src 对象
     * @return Map映射
     */
    public static Map<String, Object> parseToMap(Object src) {
        Map<String, Object> mapFieldVals = new HashMap<>();
        AsmReflectAccess accessSrc = AsmReflectWrapper.parseClassAccess(src.getClass());

        // 遍历public字段
        accessSrc.getMapPubFields().forEach((k, v) -> {
            Object t = accessSrc.getFieldAccess().get(src, v);
            if (t != null) {
                mapFieldVals.putIfAbsent(k, t);
            }
        });
        // 遍历private变量
        accessSrc.getMapGetMethods().forEach((k, v) -> {
            Object t = accessSrc.getMethodAccess().invoke(src, v);
            if (t != null) {
                mapFieldVals.putIfAbsent(k, t);
            }
        });
        return mapFieldVals;
    }

//    static DecimalFormat decimalFormat = new DecimalFormat();
//    static {
//        decimalFormat.setGroupingUsed(false);
//    }
//
//    public static <T> T objectMapToObject(Map<String, Object> map, Class<T> beanClass) {
//        if (map == null || map.size() == 0)
//            return null;
//        T obj = null;
//        PropertyDescriptor[] propertyDescriptors = null;
//        try {
//            obj = beanClass.newInstance();
//            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
//            propertyDescriptors = beanInfo.getPropertyDescriptors();
//        } catch (Exception e) {
//            LOGGER.error("获取bean的属性值异常, beanClass={}, 异常={}", beanClass.getName(), e.getMessage());
//        }
//        if (propertyDescriptors != null) {
//            for (PropertyDescriptor property : propertyDescriptors) {
//                Method setter = property.getWriteMethod();
//                Object value = map.get(property.getName());
//                if (setter != null) {
//                    try {
//                        setter.invoke(obj, value);
//                    } catch (Exception e) {
//                        LOGGER.warn("map值设置到bean({},property={}), value={}, 异常={}", beanClass.getName(), property.getName(), value, e.getMessage());
//                    }
//                }
//            }
//        }
//        return obj;
//    }
//
//    public static Map<String, Object> objectToObjectMap(Object obj) {
//        Map<String, Object> map = null;
//        try {
//            map = new HashMap<String, Object>();
//            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
//            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//            for (PropertyDescriptor property : propertyDescriptors) {
//                String key = property.getName();
//                if (key.compareToIgnoreCase("class") == 0) {
//                    continue;
//                }
//                Method getter = property.getReadMethod();
//                Object value = getter != null ? getter.invoke(obj) : null;
//                map.put(key, value);
//            }
//        } catch (Exception e) {
//            LOGGER.error("将bean属性值转换成map异常,bean={}, 异常={}", obj.getClass(), e.getMessage());
//        }
//        return map;
//    }
//
//    public static <T> T mapToObject(Map<String, String> map, Class<T> beanClass) {
//        if (map == null || map.size() == 0)
//            return null;
//        T obj = null;
//        PropertyDescriptor[] propertyDescriptors = null;
//        try {
//            obj = beanClass.newInstance();
//            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
//            propertyDescriptors = beanInfo.getPropertyDescriptors();
//        } catch (Exception e) {
//            LOGGER.error("获取bean的属性值异常, beanClass={}, 异常={}", beanClass.getName(), e.getMessage());
//        }
//        if (propertyDescriptors != null) {
//            // 2018年10月30日08:30:51 增加特征值, 方便查找bean对象出错问题
//            String idVal = "";
//            String uuidVal = "";
//            String uidVal = "";
//            StringBuilder errNames = new StringBuilder();
//            for (PropertyDescriptor property : propertyDescriptors) {
//                String propertieName = property.getName();
//                Method setter = property.getWriteMethod();
//                String value = map.get(propertieName);
//                if ("id".equals(propertieName)) {
//                    idVal = value;
//                } else if ("uuid".equals(propertieName)) {
//                    uuidVal = value;
//                } else if ("uid".equals(propertieName)) {
//                    uidVal = value;
//                }
//                if (setter != null && value != null) {
//                    try {
//                        switch (property.getPropertyType().getName()) {
//                            case "java.lang.String":
//                                setter.invoke(obj, value);
//                                break;
//                            case "java.lang.Number":
//                                setter.invoke(obj, NumberFormat.getInstance().parse(value));
//                                break;
//                            case "int":
//                                setter.invoke(obj, Integer.parseInt(value));
//                                break;
//                            case "java.lang.Integer":
//                                setter.invoke(obj, Integer.parseInt(value));
//                                break;
//                            case "double":
//                                setter.invoke(obj, Double.parseDouble(value));
//                                break;
//                            case "java.lang.Double":
//                                setter.invoke(obj, Double.parseDouble(value));
//                                break;
//                            case "long":
//                                setter.invoke(obj, Long.parseLong(value));
//                                break;
//                            case "java.lang.Long":
//                                setter.invoke(obj, Long.parseLong(value));
//                                break;
//                            case "boolean":
//                                setter.invoke(obj, Boolean.parseBoolean(value));
//                                break;
//                            case "java.lang.Boolean":
//                                setter.invoke(obj, Boolean.parseBoolean(value));
//                                break;
//                            case "java.util.Date":
//                                if (value.trim().length() > 0) {
//                                    // 当前仅当日期字符串不为空
//                                    try {
//                                        // 用带毫秒格式化类转换
//                                        setter.invoke(obj, GsonAdapter.defaultDateFormatMs.parse(value));
//                                    } catch (Exception e1) {
//                                        try {
//                                            // 用不带毫秒日期格式化类转换
//                                            setter.invoke(obj, GsonAdapter.defaultDateFormat.parse(value));
//                                        } catch (Exception e2) {
//                                            errNames.append(propertieName).append(",");
//                                            LOGGER.warn("map值设置到bean({},property={}), value={}, 异常={}", beanClass.getName(), property.getName(),
//                                                value, e2.getMessage());
//                                        }
//                                    }
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//                    } catch (Exception e) {
//                        errNames.append(propertieName).append(",");
//                        LOGGER.warn("map值设置到bean({},property={}), value={}, 异常={}", beanClass.getName(), property.getName(), value, e.getMessage());
//                    }
//                    if (errNames.length() > 0) {
//                        LOGGER.warn("map值设置到bean({})属性值失败, id={}, uuid={}, uid={},errNames={}", beanClass.getName(), property.getName(), idVal,
//                            uuidVal, uidVal, errNames.toString());
//                    }
//                }
//            }
//        }
//        return obj;
//    }
//
//    public static Map<String, String> objectToMap(Object obj) {
//        try {
//            Map<String, String> map = new HashMap<String, String>();
//            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
//            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//            for (PropertyDescriptor property : propertyDescriptors) {
//                String key = property.getName();
//                if (key.compareToIgnoreCase("class") == 0) {
//                    continue;
//                }
//                Method getter = property.getReadMethod();
//                switch (property.getPropertyType().getName()) {
//                    case "java.lang.Number":
//                        Object tmp = getter != null ? getter.invoke(obj) : null;
//                        String value = decimalFormat.format(tmp);
//                        if (value != null)
//                            map.put(key, value);
//                        break;
//                    case "java.util.Date":
//                        tmp = getter != null ? getter.invoke(obj) : null;
//                        if (tmp != null) {
//                            map.put(key, GsonAdapter.defaultDateFormatMs.format((java.util.Date) tmp));
//                        }
//                        break;
//                    default:
//                        tmp = getter != null ? getter.invoke(obj) : null;
//                        if (tmp != null)
//                            map.put(key, tmp.toString());
//                        break;
//                }
//            }
//            return map;
//        } catch (Exception e) {
//            LOGGER.error("将bean属性值转换成map异常,bean={}, 异常={}", obj.getClass(), e.getMessage());
//            return null;
//        }
//    }

}
