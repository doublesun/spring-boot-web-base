package com.ywrain.common.utils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JSON处理工具类
 *
 * @author caixiwei@youcheyihou.com
 * @since 版本:1.2.0
 */
public class JsonUtil {
    protected static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    /**
     * 默认日期格式化格式
     */
    public static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将JSON字符串转换成JSONObject
     *
     * @param jsonStr JSON字符串
     * @return JSONObject对象
     */
    public static JSONObject parse(final String jsonStr) {
        if (isJsonString(jsonStr)) {
            return JSON.parseObject(jsonStr);
        } else {
            return null;
        }
    }

    /**
     * 将JSON字符串格式化为JSONObject对象
     *
     * @param jsonStr JSON字符串
     * @return JSONObject对象
     */
    public static JSONObject parseToJsonObject(final String jsonStr) {
        return parse(jsonStr);
    }

    /**
     * 将JSON字符串某个属性值格式化为JSONObject对象<br/>
     *
     * @param jsonStr JSON字符串
     * @param key 属性值
     * @return JSONObject对象
     */
    public static JSONObject parseFieldToJsonObject(final String jsonStr, final String key) {
        JSONObject result = null;
        if (key != null) {
            JSONObject josnObject = parse(jsonStr);
            if (josnObject != null) {
                result = josnObject.getJSONObject(key);
            }
        }
        return result;
    }

    /**
     * 解析字符串的为JsonArray，如果不是JsonArray，则返回null值
     *
     * @param jsonStr JSON字符串
     * @return JSONArray对象
     */
    public static JSONArray parseToJsonArray(String jsonStr) {
        if (isJsonString(jsonStr)) {
            return JSON.parseArray(jsonStr);
        } else {
            return null;
        }
    }

    /**
     * 将JSON字符串反序列化为指定返回Optional对象
     *
     * @param jsonStr JSON字符串
     * @param clazz 指定返回类
     * @return 返回Optional对象
     */
    public static <T> Optional<T> ofObject(final String jsonStr, final Class<T> clazz) {
        try {
            return Optional.ofNullable(toObject(jsonStr, clazz));
        } catch (Exception e) {
            LOGGER.warn("ofObject方法异常,jsonStr={},clazz={},异常={}", jsonStr, (clazz == null ? "" : clazz.getCanonicalName()), e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 将JSON字符串反序列化为指定返回类对象 <br/>
     * 注意:jsonStr为空或遇到格式化异常,不外抛,返回defaultVal
     *
     * @param jsonStr JSON字符串
     * @param clazz 指定返回类
     * @param defaultVal 默认值
     * @return 指定返回类对象，如果解析失败，或者字符为空个，则返回默认自定义对象
     */
    public static <T> T toObjectWithDefault(final String jsonStr, final Class<T> clazz, T defaultVal) {
        try {
            T t = toObject(jsonStr, clazz);
            if (t != null) {
                return t;
            }
        } catch (Exception e) {
            LOGGER.warn("toObjectWithDefault方法异常,jsonStr={},clazz={},异常={}", jsonStr, (clazz == null ? "" : clazz.getCanonicalName()),
                    e.getMessage());
        }
        return defaultVal;
    }

    /**
     * 将JSON字符串反序列化为指定返回类对象
     *
     * @param jsonStr JSON字符串
     * @param clazz 指定返回类
     * @return 指定返回类对象
     */
    public static <T> T toObject(final String jsonStr, final Class<T> clazz) {
        if (isJsonString(jsonStr)) {
            return JSON.parseObject(jsonStr, clazz);
        } else {
            return null;
        }
    }

    /**
     * 将JSON字符串反序列化为指定返回类对象 <br/>
     * 注意:遇到格式化异常,不外抛,返回空对象
     *
     * @param jsonStr JSON字符串
     * @param clazz 指定返回类
     * @return 指定返回类对象
     */
    public static <T> T toObjectQuietly(final String jsonStr, final Class<T> clazz) {
        T result = null;
        try {
            result = toObject(jsonStr, clazz);
        } catch (Exception e) {
            LOGGER.warn("toObjectQuietly方法异常,jsonStr={},clazz={},异常={}", jsonStr, (clazz == null ? "" : clazz.getCanonicalName()), e.getMessage());
        }
        return result;
    }

    /**
     * 将JSON字符串中的子属性反序列化为指定返回类对象 <br/>
     * 示例: jsonStr={"id":1,"city":{"id":222,"name":"222"}}, 使用toObject(jsonStr,"city",T.class)<br/>
     *
     * @param jsonStr JSON字符串
     * @param key 子属性名称
     * @param clazz 指定返回类
     * @return 指定返回类对象
     */
    public static <T> T toObject(final String jsonStr, String key, final Class<T> clazz) {
        if (isJsonString(jsonStr)) {
            return JSON.parseObject(jsonStr).getJSONObject(key).toJavaObject(clazz);
        } else {
            return null;
        }
    }

    /**
     * 将JSON字符串中的子属性反序列化为指定返回类对象 <br/>
     * 示例: jsonStr={"id":1,"city":{"id":222,"name":"222"}}, 使用toObject(jsonStr,"city",T.class) <br/>
     * 注意:遇到格式化异常,不外抛,返回空对象
     *
     * @param jsonStr JSON字符串
     * @param key 子属性名称
     * @param clazz 指定返回类
     * @return 指定返回类对象
     */
    public static <T> T toObjectQuietly(final String jsonStr, String key, final Class<T> clazz) {
        T result = null;
        try {
            result = toObject(jsonStr, key, clazz);
        } catch (Exception e) {
            LOGGER.warn("toObjectQuietly方法异常,jsonStr={},key={},clazz={},异常={}", jsonStr, key, (clazz == null ? "" : clazz.getCanonicalName()),
                    e.getMessage());
        }
        return result;
    }

    /**
     * 将JSON字符串反序列化为指定返回类对象,针对对象存在泛型的处理<br/>
     * 参考: https://github.com/alibaba/fastjson/wiki/TypeReference<br/>
     * 
     * <pre>
     * 示例:
     * String jsonStr = "{\"errcode\":0,\"result\":\"1\"}";
     * RPCResult&lt;String&gt; rpcResult = JsonUtil.toObjectByType(jsonStr, new com.alibaba.fastjson.TypeReference<RPCResult<String>>() {}.getType());
     * </pre>
     * 
     * @param jsonStr JSON字符串
     * @param classType 指定返回类的反射类型
     * @return 指定返回类对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObjectByType(final String jsonStr, final Type classType) {
        if (isJsonString(jsonStr)) {
            return (T) JSON.parseObject(jsonStr, classType);
        } else {
            return null;
        }
    }

    /**
     * 将JSON字符串反序列化为指定返回类对象,针对对象存在泛型的处理<br/>
     * 参考: https://github.com/alibaba/fastjson/wiki/TypeReference<br/>
     * 注意:遇到格式化异常,不外抛,返回空对象
     * 
     * <pre>
     * 示例:
     * String jsonStr = "{\"errcode\":0,\"result\":\"1\"}";
     * RPCResult&lt;String&gt; rpcResult = JsonUtil.toObjectByType(jsonStr, new com.alibaba.fastjson.TypeReference<RPCResult<String>>() {}.getType());
     * </pre>
     * 
     * @param jsonStr JSON字符串
     * @param classType 指定返回类的反射类型
     * @return 指定返回类对象
     */
    public static <T> T toObjectByTypeQuietly(final String jsonStr, final Type classType) {
        T result = null;
        try {
            result = toObjectByType(jsonStr, classType);
        } catch (Exception e) {
            //ingore
            LOGGER.warn("toObjectByTypeQuietly方法异常,jsonStr={},classType={},异常={}", jsonStr, (classType == null ? "" : classType.getTypeName()),
                    e.getMessage());
        }
        return result;
    }

    /**
     * 将Json字符串转换反序列化为List&lt;T&gt;对象<br/>
     *
     * @param jsonStr JSON字符串
     * @param clazz 指定返回类
     * @return 返回指定返回类对象列表, 校验不合规, 返回空值(非空列表)
     */
    public static <T> List<T> toList(String jsonStr, final Class<T> clazz) {
        if (isJsonString(jsonStr)) {
            return JSON.parseArray(jsonStr, clazz);
        } else {
            return null;
        }
    }

    /**
     * 将Json字符串转换反序列化为List&lt;T&gt;对象<br/>
     * 注意:遇到格式化异常,不外抛,返回空对象<br/>
     *
     * @param jsonStr JSON字符串
     * @param clazz 指定返回类
     * @return 返回指定返回类对象列表, 校验不合规, 返回空值(非空列表)
     */
    public static <T> List<T> toListQuietly(String jsonStr, final Class<T> clazz) {
        List<T> result = null;
        try {
            result = toList(jsonStr, clazz);
        } catch (Exception e) {
            LOGGER.warn("toListQuietly方法异常,jsonStr={},clazz={},异常={}", jsonStr, (clazz == null ? "" : clazz.getCanonicalName()), e.getMessage());
        }
        return result;
    }

    /**
     * 将Json字符串的子属性反序列化为List&lt;T&gt;对象 <br/>
     * 示例: jsonStr={"id":1,"citys":[{"id":111,"name":"111"},{"id":222,"name":"222"}]}, 使用toObject(jsonStr,"citys",T.class)<br/>
     *
     * @param jsonStr JSON字符串
     * @param key 子属性名称
     * @param clazz 指定返回类
     * @return 返回指定返回类对象列表, 校验不合规, 返回空值(非空列表)
     */
    public static <T> List<T> toList(String jsonStr, String key, final Class<T> clazz) {
        List<T> _val = null;
        JSONObject _jsonObject = parse(jsonStr);
        if (_jsonObject != null) {
            JSONArray _jsonArry = _jsonObject.getJSONArray(key);
            if (_jsonArry != null) {
                _val = _jsonArry.toJavaList(clazz);
            }
        }
        return _val;
    }

    /**
     * 将Json字符串反序列化为List&lt;T&gt;对象 <br/>
     * 示例: jsonStr={"id":1,"citys":[{"id":111,"name":"111"},{"id":222,"name":"222"}]}, 使用toObject(jsonStr,"citys",T.class)<br/>
     * 注意:遇到格式化异常,不外抛,返回空对象<br/>
     *
     * @param jsonStr JSON字符串
     * @param key 子属性名称
     * @param clazz 指定返回类
     * @return 返回指定返回类对象列表, 校验不合规, 返回空值(非空列表)
     */
    public static <T> List<T> toListQuietly(String jsonStr, String key, final Class<T> clazz) {
        List<T> result = null;
        try {
            result = toList(jsonStr, clazz);
        } catch (Exception e) {
            LOGGER.warn("toListQuietly方法异常,jsonStr={},key={},clazz={},异常={}", jsonStr, key, (clazz == null ? "" : clazz.getCanonicalName()),
                    e.getMessage());
        }
        return result;
    }

    /**
     * 将对象转换为字符串,不包含对象中的值为null的字段
     *
     * @param Object 待序列化的对象
     * @return 返回字符串
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONStringWithDateFormat(object, DEFAULT_DATEFORMAT);
    }

    /**
     * 将对象转换为字符串,不包含对象中的值为null的字段 <br/>
     * 注意:遇到格式化异常,不外抛,返回空对象
     *
     * @param Object 待序列化的对象
     * @return 返回字符串
     */
    public static String toJsonStringQuietly(Object object) {
        String _val = null;
        try {
            _val = toJsonString(object);
        } catch (Exception e) {
            LOGGER.warn("toJsonStringQuietly方法异常,object.clazzname={},异常={}", (object == null ? "" : object.getClass().getCanonicalName()),
                    e.getMessage());
        }
        return _val;
    }

    /**
     * 将对象转换为字符串,并指定日期格式化形式
     *
     * @param object 待序列化的对象
     * @param dateFormat 指定日期格式化字符串,如:yyyy-MM-dd HH:mm:ss.SS
     * @return 返回字符串
     */
    public static String toJsonStringWithDateFormat(Object object, String dateFormat) {
        return JSON.toJSONStringWithDateFormat(object, dateFormat);
    }

    /**
     * 将对象转换为字符串,允许null值字段显示
     *
     * @param Object 待序列化的对象
     * @return 返回字符串
     */
    public static String toJsonStringWithNull(Object object) {
        return JSON.toJSONStringWithDateFormat(object, DEFAULT_DATEFORMAT, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 获取JSON字符串中某个字符串属性值<br/>
     * 注意: 字符串无法格式化,该方法会抛异常
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @return 属性值
     */
    public static String getString(final String jsonStr, final String key) {
        String _val = null;
        JSONObject jsonObject = parse(jsonStr);
        if (jsonObject != null) {
            _val = jsonObject.getString(key);
        }
        return _val;
    }

    /**
     * 获取JSON字符串中某个字符串属性值<br/>
     * 注意:字符串无法格式化,异常不外抛,返回默认值<br/>
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @param defaultVal 默认值
     * @return 属性值.如果属性值为null, 则返回默认值
     */
    public static String getString(final String jsonStr, final String key, final String defaultVal) {
        String _val = null;
        try {
            _val = getString(jsonStr, key);
        } catch (Exception e) {
            LOGGER.warn("getString方法异常,字符串={},属性名称={},defaultVal={},异常={}", jsonStr, key, defaultVal, e.getMessage());
        }
        if (_val == null) {
            return defaultVal;
        } else {
            return _val;
        }
    }

    /**
     * 获取JSON字符串中某个整形属性值<br/>
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @return 属性值
     */
    public static Integer getInt(final String jsonStr, final String key) {
        Integer _val = null;
        JSONObject jsonObject = parse(jsonStr);
        if (jsonObject != null) {
            _val = jsonObject.getInteger(key);
        }
        return _val;
    }

    /**
     * 获取JSON字符串中某个整形属性值<br/>
     * 注意:字符串无法格式化,异常不外抛,返回默认值
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @param defaultVal 默认值
     * @return 属性值.如果属性值为null, 则返回默认值
     */
    public static int getInt(final String jsonStr, final String key, final int defaultVal) {
        Integer _val = null;
        try {
            _val = getInt(jsonStr, key);
        } catch (Exception e) {
            LOGGER.warn("getInt方法异常,字符串={},属性名称={},defaultVal={},异常={}", jsonStr, key, defaultVal, e.getMessage());
        }
        if (_val == null) {
            return defaultVal;
        } else {
            return _val.intValue();
        }
    }

    /**
     * 获取JSON字符串中某个长整形属性值<br/>
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @return 属性值
     */
    public static Long getLong(final String jsonStr, final String key) {
        Long _val = null;
        JSONObject jsonObject = parse(jsonStr);
        if (jsonObject != null) {
            _val = jsonObject.getLong(key);
        }
        return _val;
    }

    /**
     * 获取JSON字符串中某个长整形属性值<br/>
     * 注意:字符串无法格式化,异常不外抛,返回默认值
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @param defaultVal 默认值
     * @return 属性值.如果属性值为null, 则返回默认值
     */
    public static long getLong(final String jsonStr, final String key, final long defaultVal) {
        Long _val = null;
        try {
            _val = getLong(jsonStr, key);
        } catch (Exception e) {
            LOGGER.warn("getLong方法异常,字符串={},属性名称={},defaultVal={},异常={}", jsonStr, key, defaultVal, e.getMessage());
        }
        if (_val == null) {
            return defaultVal;
        } else {
            return _val.longValue();
        }
    }

    /**
     * 获取JSON字符串中某个Date属性对象，仅包含日期 <br/>
     * 注意:若属性字段不存在,返回null <br/>
     * 要求格式："2019-05-03"
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @param defaultVal 默认值
     * @return Date属性对象.如果属性值不存在或为null, 则返回null
     */
    public static Date getDate(String jsonStr, String key, String formatterPattern) {
        return getDate(jsonStr, key, formatterPattern, null);
    }

    /**
     * 获取JSON字符串中某个Date属性对象，仅包含日期 <br/>
     * 注意:若属性字段不存在,返回默认值 <br/>
     * 要求格式："2019-05-03"
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @param defaultVal 默认值
     * @return Date属性对象.如果属性值不存在或为null, 则返回默认值
     */
    public static Date getDate(String jsonStr, String key, String formatterPattern, Date defaultVal) {
        String str = getString(jsonStr, key);
        if (str == null) {
            return defaultVal;
        }
        return DateUtil.parseToDate(formatterPattern, str);
    }

    /**
     * 获取JSON字符串中某个Date属性对象，包含日期+时间 <br/>
     * 注意:若属性字段不存在,返回null
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @param defaultVal 默认值
     * @return Date属性对象.如果属性值不存在或为null, 则返回null
     */
    public static Date getDatetime(String jsonStr, String key, String formatterPattern) {
        return getDatetime(jsonStr, key, formatterPattern, null);
    }

    /**
     * 获取JSON字符串中某个Date属性对象 <br/>
     * 注意:若属性字段不存在,返回默认值
     *
     * @param jsonStr JSON字符串
     * @param key 属性名称
     * @param defaultVal 默认值
     * @return Date属性对象.如果属性值不存在或为null, 则返回默认值
     */
    public static Date getDatetime(String jsonStr, String key, String formatterPattern, Date defaultVal) {
        String str = getString(jsonStr, key);
        if (str == null) {
            return defaultVal;
        }
        return DateUtil.parseToDatetime(formatterPattern, str);
    }

    /* = = = = = = = = = =*/

    /**
     * 简单但不严格的校验字符串是否为json字符串
     *
     * @param jsonStr 字符串
     * @return 是否json字符串:true-是,false-不是
     */
    protected static boolean isJsonString(final String jsonStr) {
        boolean _val = true;
        if (StringUtil.isBlank(jsonStr)) {
            _val = false;
        } else {
            String str = StringUtil.trim(jsonStr);
            // 兼容前端传null或者undefined的错误对象错误字符串
            if (StringUtil.equalsAnyIgnoreCase(str, "null", "undefined")) {
                _val = false;
            }
        }
        return _val;
    }
}
