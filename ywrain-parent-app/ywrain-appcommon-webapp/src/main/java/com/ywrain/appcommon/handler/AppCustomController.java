package com.ywrain.appcommon.handler;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ywrain.appcommon.config.ApiWebConfig;
import com.ywrain.appcommon.consts.ReqConsts;
import com.ywrain.appcommon.proto.Response;
import com.ywrain.appcommon.utils.RespUtil;

/**
 * SpringMVC自定义页面响应。注意：这里并没有特殊处理旧协议版本的返回
 *
 * @author xuguangming@ywrain.com
 */
@RestController
public class AppCustomController implements ErrorController {

    /**
     * 新增授权请求处理
     */
    @RequestMapping(value = "/**", method = {RequestMethod.OPTIONS})
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Response corsOptions() {
        return null;
    }

    /**
     * 错误页响应
     */
    @RequestMapping(value = ApiWebConfig.ERR_PATH)
    public Response errorPage() {
        return RespUtil.getErrParams(ReqConsts.REQ_ERR_MSG);
    }

    @Override
    public String getErrorPath() {
        return ApiWebConfig.ERR_PATH;
    }
}
