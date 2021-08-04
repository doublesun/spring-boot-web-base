package com.ywrain.common.support;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.ywrain.common.utils.StringUtil;

/**
 * 对象的ASM反射访问封装代理方法
 *
 * @author weipengfei@youcheyihou.com
 */
public class AsmReflectWrapper {

    public static Map<String, AsmReflectAccess> mapAccess = new ConcurrentHashMap<String, AsmReflectAccess>();

    /**
     * 解析对象的成员变量映射
     *
     * @param clazz 成员类
     * @return 映射表
     */
    public static Map<String, Field> parseFields(Class<?> clazz) {
        Map<String, Field> mapFields = new HashMap<>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isPrivate(field.getModifiers()) || Modifier.isProtected(field.getModifiers())) {
                    mapFields.putIfAbsent(field.getName(), field); // 名称相同时，取子类字段
                }
            }
            // 遍历父类
            clazz = clazz.getSuperclass();
        }
        return mapFields;
    }

    /**
     * 解析获取类的可访问字段和方法，并缓存
     * <br> 内部使用JAVA原生反射，和ReflectASM反射
     * <br> 对比之前版本ASMBeanUtil工具类对父类成员变量的处理，修改fields获取方法，支持父类public成员变量字段获取
     *
     * @param obj 类
     * @return 可获得方法
     */
    public static AsmReflectAccess parseClassAccess(Class<?> clazz) {
        AsmReflectAccess asmReflectAccess = mapAccess.get(clazz.getName());
        if (null != asmReflectAccess) {
            return asmReflectAccess;
        }
        asmReflectAccess = new AsmReflectAccess();
        asmReflectAccess.setClazzName(clazz.getName());
        synchronized (clazz) {
            // 缓存方法
            Map<String, Integer> mapGetMethods = new HashMap<>();
            Map<String, Integer> mapSetMethods = new HashMap<>();
            Map<String, Integer> mapPubFields = new HashMap<>(2);
            Map<String, Field> mapAllFields = new HashMap<>();

            // 遍历public成员字段（包括父类）
            FieldAccess fieldAccess = FieldAccess.get(clazz);
            for (Field field : fieldAccess.getFields()) {
                mapPubFields.putIfAbsent(field.getName(), fieldAccess.getIndex(field.getName()));
                mapAllFields.putIfAbsent(field.getName(), field);
            }
            // 遍历private成员字段列表，包括父类， 名称相同时，取子类字段
            MethodAccess methodAccess = MethodAccess.get(clazz);
            Class<?> superClazz = clazz;
            while (superClazz != null) {
                Field[] fields = superClazz.getDeclaredFields();
                for (Field field : fields) {
                    if (Modifier.isPrivate(field.getModifiers()) || Modifier.isProtected(field.getModifiers())) {
                        String suffixFieldName = StringUtil.capitalize(field.getName());
                        // 绑定getter/setter方法映射
                        if ("boolean".equals(field.getType().getName()) || "java.lang.Boolean".equals(field.getType().getName())) {
                            try {
                                int idxGetter = methodAccess.getIndex("is" + suffixFieldName);
                                mapGetMethods.putIfAbsent(field.getName(), idxGetter);
                            } catch (Exception e) {
                                int idxGetter = methodAccess.getIndex("get" + suffixFieldName);
                                mapGetMethods.putIfAbsent(field.getName(), idxGetter);
                            }
                        } else {
                            int idxGetter = methodAccess.getIndex("get" + suffixFieldName);
                            mapGetMethods.putIfAbsent(field.getName(), idxGetter);
                        }
                        int idxSetter = methodAccess.getIndex("set" + suffixFieldName);
                        mapSetMethods.putIfAbsent(field.getName(), idxSetter);

                        mapAllFields.putIfAbsent(field.getName(), field);
                    }
                }
                // 遍历父类
                superClazz = superClazz.getSuperclass();
            }
            asmReflectAccess.setMethodAccess(methodAccess);
            asmReflectAccess.setFieldAccess(fieldAccess);
            asmReflectAccess.setMapPubFields(mapPubFields);
            asmReflectAccess.setMapGetMethods(mapGetMethods);
            asmReflectAccess.setMapSetMethods(mapSetMethods);
            asmReflectAccess.setMapAllFields(mapAllFields);
            mapAccess.putIfAbsent(clazz.getName(), asmReflectAccess);
        }

        return asmReflectAccess;
    }
}
