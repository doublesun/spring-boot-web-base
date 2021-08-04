package com.ywrain.appcommon.proto;

import com.ywrain.appcommon.consts.ProtoConsts;
import com.ywrain.appcommon.enums.ErrCode;

/**
 * 协议响应定义
 * 
 * @author xuguangming@ywrain.com
 * @date 2017年3月24日
 */
public class Response {
    /**
     * 返回成功响应，不自定义result对象
     * 
     * <pre>
     *  {"errcode":0}
     * </pre>
     * 
     * @return Response 响应对象
     */
    public static Response ok() {
        return new Response(null);
    }

    /**
     * 返回成功响应，自定义result对象
     * 
     * <pre>
     *  {"errcode":0, "result": 自定义result对象}
     * </pre>
     * 
     * @param result 自定义result对象
     * @return Response 响应对象
     */
    public static Response ok(Object result) {
        return new Response(result);
    }

    /**
     * 返回身份验证错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.AUTH_ERR, "msg": ProtoConsts.ERR_MSG_AUTH}
     * </pre>
     * 
     * @return Response 响应对象
     */
    public static Response errAuth() {
        return errAuth(ProtoConsts.ERR_MSG_AUTH);
    }

    /**
     * 返回身份验证错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.AUTH_ERR, "msg": 自定义消息}
     * </pre>
     * 
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response errAuth(String msg) {
        return errAuth(msg, null);
    }

    /**
     * 返回身份验证错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.AUTH_ERR, "msg": 自定义消息, "result": 自定义result对象}
     * </pre>
     * 
     * @param msg 自定义消息
     * @param result 自定义result对象
     * @return Response 响应对象
     */
    public static Response errAuth(String msg, Object result) {
        return new Response(ErrCode.AUTH_ERR.getValue(), msg, result);
    }

    /**
     * 返回参数错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.PARAMS_ERR, "msg":  ProtoConsts.ERR_MSG_PARAM}
     * </pre>
     * 
     * @return Response 响应对象
     */
    public static Response errParams() {
        return errParams(ProtoConsts.ERR_MSG_PARAM);
    }

    /**
     * 返回参数错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.PARAMS_ERR, "msg": 自定义消息}
     * </pre>
     * 
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response errParams(String msg) {
        return errParams(msg, null);
    }

    /**
     * 返回参数错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.PARAMS_ERR, "msg": 自定义消息, "result": 自定义result对象}
     * </pre>
     * 
     * @param msg 自定义消息
     * @param result 自定义result对象
     * @return Response 响应对象
     */
    public static Response errParams(String msg, Object result) {
        return new Response(ErrCode.PARAMS_ERR.getValue(), msg, result);
    }

    /**
     * 返回业务处理错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.BIZ_ERR, "msg": ProtoConsts.ERR_MSG_BIZ}
     * </pre>
     * 
     * @return Response 响应对象
     */
    public static Response errBiz() {
        return errBiz(ProtoConsts.ERR_MSG_BIZ);
    }

    /**
     * 返回业务处理错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.BIZ_ERR, "msg": 自定义消息}
     * </pre>
     * 
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response errBiz(String msg) {
        return errBiz(msg, null);
    }

    /**
     * 返回业务处理错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.BIZ_ERR, "msg": 自定义消息, "result": 自定义result对象}
     * </pre>
     * 
     * @param msg 自定义消息
     * @param result 自定义result对象
     * @return Response 响应对象
     */
    public static Response errBiz(String msg, Object result) {
        return new Response(ErrCode.BIZ_ERR.getValue(), msg, result);
    }

    /**
     * 返回系统处理错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.SYS_ERR, "msg": ProtoConsts.ERR_MSG_SYS}
     * </pre>
     * 
     * @return Response 响应对象
     */
    public static Response errSys() {
        return errSys(ProtoConsts.ERR_MSG_SYS);
    }

    /**
     * 返回系统处理错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.SYS_ERR, "msg": 自定义消息}
     * </pre>
     * 
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response errSys(String msg) {
        return errSys(msg, null);
    }

    /**
     * 返回系统处理错误响应
     * 
     * <pre>
     *  {"errcode": ErrCode.SYS_ERR, "msg": 自定义消息, "result": 自定义result对象}
     * </pre>
     * 
     * @param msg 自定义消息
     * @param result 自定义result对象
     * @return Response 响应对象
     */
    public static Response errSys(String msg, Object result) {
        return new Response(ErrCode.SYS_ERR.getValue(), msg, result);
    }

    /**
     * 返回用户冻结响应
     * 
     * <pre>
     *  {"errcode": ErrCode.FROZENED_ERR,, "msg": ProtoConsts.ERR_MSG_USER_FROZENED}
     * </pre>
     * 
     * @return Response 响应对象
     */
    public static Response errFrozened() {
        return errFrozened(ProtoConsts.ERR_MSG_USER_FROZENED);
    }

    /**
     * 返回用户冻结响应
     * 
     * <pre>
     *  {"errcode": ErrCode.FROZENED_ERR,, "msg": 自定义消息}
     * </pre>
     * 
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response errFrozened(String msg) {
        return errFrozened(msg, null);
    }

    /**
     * 返回用户冻结响应
     * 
     * <pre>
     *  {"errcode": ErrCode.FROZENED_ERR,, "msg": 自定义消息, "result": 自定义result对象}
     * </pre>
     * 
     * @param msg 自定义消息
     * @param result 自定义result对象
     * @return Response 响应对象
     */
    public static Response errFrozened(String msg, Object result) {
        return new Response(ErrCode.FROZENED_ERR.getValue(), msg, result);
    }

    /**
     * 返回用户禁言响应
     * 
     * <pre>
     *  {"errcode": ErrCode.FORBIDDEN_ERR, "msg": ProtoConsts.ERR_MSG_USER_FORBIDDEN}
     * </pre>
     * 
     * @return Response 响应对象
     */
    public static Response errForbidden() {
        return errForbidden(ProtoConsts.ERR_MSG_USER_FORBIDDEN);
    }

    /**
     * 返回用户禁言响应
     * 
     * <pre>
     *  {"errcode": ErrCode.FORBIDDEN_ERR, "msg": 自定义消息}
     * </pre>
     * 
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response errForbidden(String msg) {
        return errForbidden(msg, null);
    }

    /**
     * 返回用户禁言响应
     * 
     * <pre>
     *  {"errcode": ErrCode.FORBIDDEN_ERR, "msg": 自定义消息, "result": 自定义result对象}
     * </pre>
     * 
     * @param msg 自定义消息
     * @param result 自定义result对象
     * @return Response 响应对象
     */
    public static Response errForbidden(String msg, Object result) {
        return new Response(ErrCode.FORBIDDEN_ERR.getValue(), msg, result);
    }

    /**
     * 返回响应，权限错误，是指对某接口资源的访问或操作权限
     * 
     * <pre>
     *  {"errcode": ErrCode.FROZENED_ERR, "msg": 自定义消息}
     * </pre>
     * 
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response errPermission(String msg) {
        return errPermission(msg, null);
    }

    /**
     * 返回响应，权限错误，是指对某接口资源的访问或操作权限
     * 
     * <pre>
     *  {"errcode": ErrCode.FROZENED_ERR, "msg": 自定义消息}
     * </pre>
     * 
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response errPermission(String msg, Object result) {
        return new Response(ErrCode.FROZENED_ERR.getValue(), msg, result);
    }

    /**
     * 返回响应，自定义错误码和消息
     * 
     * @param errcode 指定的错误码
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response build(Integer errcode, String msg) {
        return build(errcode, msg, null);
    }

    /**
     * 返回响应，自定义错误码和消息，附带data
     * 
     * @param errcode 指定的错误码
     * @param msg 自定义消息
     * @return Response 响应对象
     */
    public static Response build(Integer errcode, String msg, Object result) {
        return new Response(errcode, msg, result);
    }

    /**
     * 根据错误消息返回正确的响应对象<br>
     * <p>
     * errMsg为空, 则返回成功: {"errcode":0}<br>
     * errMsg不为空, 则返回业务处理失败,并将错误消息作为自定义消息字段值: {"errcode": ErrCode.BIZ_ERR, "msg": errMsg}<br>
     * <p>
     * 
     * @param errMsg 错误消息
     * @return Response 响应对象
     */
    public static Response buildByErrMsg(String errMsg) {
        if (errMsg == null || "".equals(errMsg.trim())) {
            return ok();
        } else {
            return errBiz(errMsg);
        }
    }

    /**
     * 系统错误码，0：成功
     */
    protected int errcode = ErrCode.NO_ERR.getValue();
    /**
     * 系统消息
     */
    protected String msg;
    /**
     * 返回数据
     */
    private Object result;

    public Response(Object result) {
        this.result = result;
    }

    public Response(int errcode, String msg, Object result) {
        this.errcode = errcode;
        this.msg = msg;
        this.result = result;
    }

    public Response(int errcode, String msg) {
        this.errcode = errcode;
        this.msg = msg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
