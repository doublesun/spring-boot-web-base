package com.ywrain.appcommon.utils;

import java.io.IOException;
import java.text.DecimalFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.ywrain.appcommon.enums.ErrCode;
import com.ywrain.appcommon.proto.Response;

/**
 * 返回响应的通用封装工具类
 *
 * @author xuguangming@ywrain.com
 * @date 2018年3月28日
 **/
public class RespUtil {

    public static final String defaultDateFormatPattern = "yyyy-MM-dd HH:mm:ss";

    public static class DoubleTypeAdapter extends TypeAdapter<Double> {
        @Override
        public void write(JsonWriter out, Double value) throws IOException {
            if (value == null) {
                // out.nullValue();
                out.value(0.00d); // 序列化时将 null 转为 0.00d
            } else {
                DecimalFormat format = new DecimalFormat("#0.##"); // 保留两位
                String temp = format.format(value);
                out.value(Double.valueOf(temp));
            }
        }

        @Override
        public Double read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return in.nextDouble();
        }
    }

    public static Gson gson = new GsonBuilder()
        // 日期格式
        .setDateFormat(defaultDateFormatPattern)
        // 浮点数处理，默认小数点后2位
        .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
        .create();

    /**
     * 打包协议对象数据，转换为字符串
     *
     * @param resp 数据对象
     * @return String
     * @date 2018年3月28日
     */
    public static <T> String pack(T resp) {
        if (resp instanceof String) {
            return (String) resp;
        }
        return RespUtil.gson.toJson(resp);
    }

    /**
     * 返回成功响应，附带消息和数据result字段
     *
     * <pre>
     *  {"errcode":0, "msg":"its a msg"}
     *  {"errcode":0, "msg":"its a msg", "result":{"key":"value"}}
     * </pre>
     *
     * @param msg 附带消息
     * @param result 数据Object对象，允许null
     * @return Response
     */
    public static Response getOk(String msg, Object result) {
        return Response.build(ErrCode.NO_ERR.getValue(), msg, result);
    }

    /**
     * 返回成功响应，附带数据result字段
     *
     * <pre>
     *  {"errcode":0}
     *  {"errcode":0, "result":{"key":"value"}}
     * </pre>
     *
     * @param result 数据Object对象，允许null
     * @return Response
     */
    public static Response getOk(Object result) {
        return Response.ok(result);
    }

    /**
     * 返回成功响应，不附带数据
     *
     * <pre>
     *  {"errcode":0}
     * </pre>
     *
     * @return Response
     */
    public static Response getOk() {
        return Response.ok();
    }

    /**
     * 返回响应，身份验证错误
     *
     * <pre>
     *  {"errcode":0, "msg":"校验错误，请登录xxxx"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrAuth(String msg) {
        return Response.errAuth(msg);
    }

    /**
     * 返回响应，身份验证错误
     *
     * <pre>
     *  {"errcode":0, "msg":"校验错误，请登录xxxx"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrAuth(String msg, Object result) {
        return Response.errAuth(msg, result);
    }

    /**
     * 返回响应，参数错误
     *
     * <pre>
     *  {"errcode":0, "msg":"参数错误，请重新输入"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrParams(String msg) {
        return Response.errParams(msg);
    }

    /**
     * 返回响应，参数错误
     *
     * <pre>
     *  {"errcode":0, "msg":"参数错误，请重新输入"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrParams(String msg, Object result) {
        return Response.errParams(msg, result);
    }

    /**
     * 业务处理错误，可显示
     *
     * <pre>
     *  {"errcode":0, "msg":"xx失败"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrBiz(String msg) {
        return Response.errBiz(msg);
    }

    /**
     * 业务处理错误，可显示
     *
     * <pre>
     *  {"errcode":0, "msg":"xx失败"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrBiz(String msg, Object result) {
        return Response.errBiz(msg, result);
    }

    /**
     * 返回响应，系统处理异常
     *
     * <pre>
     *  {"errcode":400, "msg":"系统运行异常"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrSys(String msg) {
        return Response.errSys(msg);
    }

    /**
     * 返回响应，系统处理异常
     *
     * <pre>
     *  {"errcode":400, "msg":"系统运行异常"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrSys(String msg, Object result) {
        return Response.errSys(msg, result);
    }

    /**
     * 返回响应，权限错误：冻结
     *
     * <pre>
     *  {"errcode":120, "msg":"权限错误：冻结"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrFrozened(String msg) {
        return Response.errFrozened(msg);
    }

    /**
     * 返回响应，权限错误：冻结
     *
     * <pre>
     *  {"errcode":120, "msg":"权限错误：冻结"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrFrozened(String msg, Object result) {
        return Response.errFrozened(msg, result);
    }

    /**
     * 返回响应，权限错误：禁言
     *
     * <pre>
     *  {"errcode":121, "msg":"权限错误：禁言"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrForbidden(String msg) {
        return Response.errForbidden(msg);
    }

    /**
     * 返回响应，权限错误：禁言
     *
     * <pre>
     *  {"errcode":121, "msg":"权限错误：禁言"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrForbidden(String msg, Object result) {
        return Response.errForbidden(msg, result);
    }

    /**
     * 返回响应，权限错误，是指对某接口资源的访问或操作权限
     *
     * <pre>
     *  {"errcode":121, "msg":"权限限制"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrPermission(String msg) {
        return Response.errPermission(msg);
    }

    /**
     * 返回响应，权限错误，是指对某接口资源的访问或操作权限
     *
     * <pre>
     *  {"errcode":121, "msg":"权限限制"}
     * </pre>
     *
     * @param msg 消息提示
     * @return Response
     */
    public static Response getErrPermission(String msg, Object result) {
        return Response.errPermission(msg, result);
    }

    /**
     * 打包返回响应，自定义错误码和消息
     *
     * @param errcode 指定的错误码
     * @param msg 消息提示
     * @return Response
     * @date 2018年3月28日
     */
    public static Response getErr(Integer errcode, String msg) {
        return Response.build(errcode, msg);
    }

    /**
     * 打包返回响应，自定义错误码和消息，附带data
     *
     * @param errcode 指定的错误码
     * @param msg 消息提示
     * @param result 附带数据，JsonObjectString
     * @return Response
     * @date 2018年3月28日
     */
    public static Response getErr(Integer errcode, String msg, Object result) {
        return Response.build(errcode, msg, result);
    }
}
