package com.ywrain.appcommon.exception;


import com.ywrain.appcommon.proto.Response;
import com.ywrain.appcommon.utils.RespUtil;

/**
 * 自定义异常处理类
 * 
 * @author xuguangming@ywrain.com
 *
 */
@SuppressWarnings("serial")
public class AppCustomException extends RuntimeException {

    private Response response;

    public AppCustomException(Response response) {
        super(response.getMsg());
        this.response = response;
    }

    @Override
    public String getMessage() {
        return RespUtil.pack(response);
    }

    public Response getResponse() {
        return response;
    }

}
