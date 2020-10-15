package com.luxx.seed.web.resolver;

import com.luxx.seed.constant.ErrorCode;
import com.luxx.seed.model.Response;
import com.luxx.seed.model.ResponseUtil;
import com.luxx.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackages = "com.luxx.seed.controller")
@Slf4j
public class ResponseBodyAnalysis implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Response) {
            return body;
        }
        if (body instanceof String) {
            return JsonUtil.encode(ResponseUtil.success(body));
        }
        return ResponseUtil.success(body);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Throwable.class})
    public Object handleException(Throwable e) {
        return ResponseUtil.fail(ErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
    }
}
