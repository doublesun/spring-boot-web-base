package com.ywrain.common.support;

import java.lang.reflect.Field;
import java.util.Map;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * 对象的ASM反射访问封装
 *
 * @author weipengfei@youcheyihou.com
 */
public class AsmReflectAccess {
    private String clazzName;
    private MethodAccess methodAccess;
    private FieldAccess FieldAccess;
    private Map<String, Integer> mapGetMethods;
    private Map<String, Integer> mapSetMethods;
    private Map<String, Integer> mapPubFields;
    private Map<String, Field> mapAllFields;

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public Map<String, Integer> getMapGetMethods() {
        return mapGetMethods;
    }

    public void setMapGetMethods(Map<String, Integer> mapGetMethods) {
        this.mapGetMethods = mapGetMethods;
    }

    public Map<String, Integer> getMapSetMethods() {
        return mapSetMethods;
    }

    public void setMapSetMethods(Map<String, Integer> mapSetMethods) {
        this.mapSetMethods = mapSetMethods;
    }

    public FieldAccess getFieldAccess() {
        return FieldAccess;
    }

    public void setFieldAccess(FieldAccess fieldAccess) {
        FieldAccess = fieldAccess;
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    public void setMethodAccess(MethodAccess methodAccess) {
        this.methodAccess = methodAccess;
    }

    public Map<String, Integer> getMapPubFields() {
        return mapPubFields;
    }

    public void setMapPubFields(Map<String, Integer> mapPubFields) {
        this.mapPubFields = mapPubFields;
    }

    public Map<String, Field> getMapAllFields() {
        return mapAllFields;
    }

    public void setMapAllFields(Map<String, Field> mapAllFields) {
        this.mapAllFields = mapAllFields;
    }
}
