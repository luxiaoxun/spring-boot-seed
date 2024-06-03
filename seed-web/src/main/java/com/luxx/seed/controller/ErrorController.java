package com.luxx.seed.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.luxx.seed.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Response handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage());
        return ResponseUtil.fail(ResponseCode.PARAM_ERROR);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public Response handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage());
        return ResponseUtil.fail(ResponseCode.PARAM_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        return ResponseUtil.generateResult(ResponseCode.PARAM_ERROR, e.getLocalizedMessage().split(";")[0]);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Response handleHttpMessageNotReadableException(BindException e) {
        log.error(e.getMessage());
        Map map = new HashMap<String, Object>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            map.put(error.getField(), error.getRejectedValue());
        }
        return ResponseUtil.generateResult(ResponseCode.PARAM_ERROR, map);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        Map map = new HashMap<String, Object>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            map.put(error.getField(), error.getRejectedValue());
        }
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseUtil.generateResult(ResponseCode.PARAM_ERROR.getCode(), message, map);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Response handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage());
        Map<String, Object> map = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation c : constraintViolations) {
            String field = null;
            for (Path.Node node : c.getPropertyPath()) {
                field = node.getName();
            }
            map.put(field, c.getInvalidValue() == null ? "null" : c.getInvalidValue().toString());
        }
        return ResponseUtil.generateResult(ResponseCode.PARAM_ERROR, map);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("method", e.getHttpMethod());
        map.put("url", e.getRequestURL());
        return ResponseUtil.generateResult(ResponseCode.PARAM_ERROR, map);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage());
        return ResponseUtil.generateResult(ResponseCode.PARAM_ERROR, e.getMethod());
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public Response handleNotLoginException(Exception e) {
        log.error(e.getMessage());
        log.error("Not login exception: {}", e.toString());
        return ResponseUtil.fail(ResponseCode.NO_PERMISSION);
    }

    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public Response handleNotPermissionException(Exception e) {
        log.error(e.getMessage());
        log.error("Not permission exception: {}", e.toString());
        return ResponseUtil.fail(ResponseCode.NO_PERMISSION);
    }

    @ExceptionHandler(NotRoleException.class)
    @ResponseBody
    public Response handleNotRoleException(Exception e) {
        log.error(e.getMessage());
        log.error("Not role exception: {}", e.toString());
        return ResponseUtil.fail(ResponseCode.NO_PERMISSION);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(Exception e) {
        log.error(e.getMessage());
        log.error("Internal error, exception:", e);
        return ResponseUtil.fail();
    }
}