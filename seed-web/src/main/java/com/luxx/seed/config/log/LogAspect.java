package com.luxx.seed.config.log;

import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.util.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Aspect
//@Configuration
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMappingPointCut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMappingPointCut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMappingPointCut() {
    }

    @Around("postMappingPointCut() || getMappingPointCut() || requestMappingPointCut()")
    public Object logControllerInAndOut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        long start = System.currentTimeMillis();
        Signature signature = joinPoint.getSignature();
        String param = "";
        //Can not serialize BasicErrorController with json format
        if (!signature.getName().equals("error")) {
            param = JsonUtil.encode(joinPoint.getArgs());
        }
        try {
            result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            logger.info("method={}||execute_time={}||param={}||result={}", signature.toString(), end - start, param, JsonUtil.encode(result));
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            result = ResponseUtil.fail(ResponseCode.SYSTEM_ERROR);
            logger.error("method={}||execute_time={}||param={}||error=", signature.toString(), end - start, param, e);
        }
        return result;
    }

    @Around("execution(* com.luxx.seed.service.*.*(..))")
    public Object logServiceInAndOut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        long start = System.currentTimeMillis();
        Signature signature = joinPoint.getSignature();
        String param = JsonUtil.encode(joinPoint.getArgs());
        try {
            result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            logger.info("method={}||execute_time={}||param={}||result={}", signature.toString(), end - start, param, JsonUtil.encode(result));
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            logger.error("method={}||execute_time={}||param={}||error=", signature.toString(), end - start, param, e);
            throw e;
        }
        return result;
    }
}
