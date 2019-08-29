package com.watermelon.mall.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.watermelon.common.framework.util.ExceptionUtil;
import com.watermelon.common.framework.util.HttpUtil;
import com.watermelon.common.framework.util.MallUtil;
import com.watermelon.common.framework.vo.CommonResult;
import com.watermelon.mall.admin.api.SystemLogService;
import com.watermelon.mall.admin.api.dto.systemlog.AccessLogAddDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 访问日志拦截器
 */
@Component
public class AccessLogInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    private static final ThreadLocal<Date> START_TIME = new ThreadLocal<>();

    @Reference(validation = "true", version = "${dubbo.consumer.AdminAccessLogService.version:1.0.0}")
    private SystemLogService systemLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        START_TIME.set(new Date());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AccessLogAddDTO accessLog = new AccessLogAddDTO();
        try {
            initAccessLog(accessLog, request);
            addAccessLog(accessLog);
        } catch (Throwable th) {
            logger.error("[afterCompletion][插入访问日志({})发生异常({})]", JSON.toJSONString(accessLog), ExceptionUtil.getRootCauseMessage(th));
        } finally {
            clear();
        }
    }

    private void initAccessLog(AccessLogAddDTO accessLog, HttpServletRequest request) {
        accessLog.setUserId(MallUtil.getUserId(request));
        if (accessLog.getUserId() == null) {
            accessLog.setUserId(AccessLogAddDTO.USER_ID_NULL);
        }
        accessLog.setUserType(MallUtil.getUserType(request));

        CommonResult result = MallUtil.getCommonResult(request);
        Assert.isTrue(result != null, "result 必须为非空");
        accessLog.setErrorCode(result.getCode())
                .setErrorMessage(result.getMessage());
        accessLog.setTraceId(MallUtil.getTraceId())
                .setApplicationName(applicationName)
                .setUri(request.getRequestURI())
                .setQueryString(request.getQueryString())
                .setMethod(request.getMethod())
                .setUserAgent(HttpUtil.getUserAgent(request))
                .setIp(HttpUtil.getIp(request))
                .setStartTime(START_TIME.get())
                .setResponseTime((int) (System.currentTimeMillis() - accessLog.getStartTime().getTime()));
    }

    private void addAccessLog(AccessLogAddDTO accessLog) {
        systemLogService.addAccessLog(accessLog);
    }

    public void clear() {
        START_TIME.remove();
    }
}
