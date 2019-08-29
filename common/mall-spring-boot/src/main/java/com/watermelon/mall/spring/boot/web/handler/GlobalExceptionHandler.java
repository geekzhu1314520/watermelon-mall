package com.watermelon.mall.spring.boot.web.handler;

import com.alibaba.fastjson.JSON;
import com.watermelon.common.framework.constant.SysErrorCodeEnum;
import com.watermelon.common.framework.exception.ServiceException;
import com.watermelon.common.framework.util.ExceptionUtil;
import com.watermelon.common.framework.util.HttpUtil;
import com.watermelon.common.framework.util.MallUtil;
import com.watermelon.common.framework.vo.CommonResult;
import com.watermelon.mall.admin.api.SystemLogService;
import com.watermelon.mall.admin.api.dto.systemlog.ExceptionLogAddDTO;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 异常总数 Metrics
     */
    private static final Counter EXCEPTION_COUNTER = Metrics.counter("mall.exception.total");

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    @Reference(validation = "true", version = "${dubbo.consumer.AdminAccessLogService.version:1.0.0}")
    private SystemLogService systemLogService;

    //逻辑异常
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult serviceExceptionHandler(HttpServletRequest request, ServiceException ex) {
        logger.debug("[serviceExceptionHandler]", ex);
        return CommonResult.error(ex.getCode(), ex.getMessage());
    }

    //参数缺失异常
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult missingServletRequestParameterExceptionHandler(HttpServletRequest request, MissingServletRequestParameterException ex) {
        logger.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return CommonResult.error(SysErrorCodeEnum.MISSING_REQUEST_PARAM_ERROR.getCode(), SysErrorCodeEnum.MISSING_REQUEST_PARAM_ERROR.getMessage());
    }

    //参数校验不正确异常
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException ex) {
        logger.info("[constraintViolationExceptionHandler]", ex);
        StringBuilder detailMessage = new StringBuilder("\n\n详细错误如下：");
        ex.getConstraintViolations().forEach(constraintViolation -> detailMessage.append("\n").append(constraintViolation.getMessage()));
        return CommonResult.error(SysErrorCodeEnum.VIOLATION_REQUEST_PARAM_ERROR.getCode()
                , SysErrorCodeEnum.VIOLATION_REQUEST_PARAM_ERROR.getMessage() + detailMessage);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult exceptionHandler(HttpServletRequest request, Exception ex) {
        logger.error("[exceptionHandler]", ex);
        ExceptionLogAddDTO exceptionLog = new ExceptionLogAddDTO();
        try {
            EXCEPTION_COUNTER.increment();
            initExceptionLog(exceptionLog, request, ex);
            addExceptionLog(exceptionLog);
        } catch (Throwable th) {
            logger.error("[exceptionHandler][插入访问日志({})发生异常({})]", JSON.toJSONString(exceptionLog), ExceptionUtils.getRootCauseMessage(ex));
        }
        return CommonResult.error(SysErrorCodeEnum.SYS_ERROR.getCode(), SysErrorCodeEnum.SYS_ERROR.getMessage());
    }

    private void initExceptionLog(ExceptionLogAddDTO exceptionLog, HttpServletRequest request, Exception ex) {
        exceptionLog.setUserId(MallUtil.getUserId(request));
        if (exceptionLog.getUserId() == null) {
            exceptionLog.setUserId(ExceptionLogAddDTO.USER_ID_NULL);
        }
        exceptionLog.setUserType(MallUtil.getUserType(request));
        exceptionLog.setApplicationName(applicationName);
        exceptionLog.setUri(request.getRequestURI());
        exceptionLog.setQueryString(request.getQueryString());
        exceptionLog.setMethod(request.getMethod());
        exceptionLog.setUserAgent(HttpUtil.getUserAgent(request));
        exceptionLog.setIp(HttpUtil.getIp(request));
        exceptionLog.setExceptionTime(new Date());
        exceptionLog.setExceptionName(ex.getClass().getName());
        exceptionLog.setExceptionMessage(ExceptionUtil.getMessage(ex));
        exceptionLog.setExceptionRootCauseMessage(ExceptionUtil.getRootCauseMessage(ex));
        exceptionLog.setExceptionStackTrace(ExceptionUtil.getStackTrace(ex));
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        Assert.notEmpty(stackTraceElements, "异常 stackTraceElements 不能为空");
        StackTraceElement stackTraceElement = stackTraceElements[0];
        exceptionLog.setExceptionClassName(stackTraceElement.getClassName());
        exceptionLog.setExceptionFileName(stackTraceElement.getFileName());
        exceptionLog.setExceptionMethodName(stackTraceElement.getMethodName());
        exceptionLog.setExceptionLineNumber(stackTraceElement.getLineNumber());
    }

    private void addExceptionLog(ExceptionLogAddDTO exceptionLog) {
        systemLogService.addExceptionLog(exceptionLog);
    }


}
