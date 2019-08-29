package com.watermelon.common.framework.util;

import com.watermelon.common.framework.constant.MallConstants;
import com.watermelon.common.framework.vo.CommonResult;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

import javax.servlet.ServletRequest;
import java.util.UUID;

public class MallUtil {

    public static Integer getUserId(ServletRequest request) {
        return (Integer) request.getAttribute(MallConstants.REQUEST_ATTR_USER_ID_KEY);
    }

    public static void setUserId(ServletRequest request, Integer userId) {
        request.setAttribute(MallConstants.REQUEST_ATTR_USER_ID_KEY, userId);
    }

    public static Integer getUserType(ServletRequest request) {
        return (Integer) request.getAttribute(MallConstants.REQUEST_ATTR_USER_TYPE_KEY);
    }

    public static void setUserType(ServletRequest request, Integer userType) {
        request.setAttribute(MallConstants.REQUEST_ATTR_USER_TYPE_KEY, userType);
    }

    public static CommonResult getCommonResult(ServletRequest request) {
        return (CommonResult) request.getAttribute(MallConstants.REQUEST_ATTR_COMMON_RESULT);
    }

    public static void setCommonResult(ServletRequest request, CommonResult result) {
        request.setAttribute(MallConstants.REQUEST_ATTR_COMMON_RESULT, result);
    }

    public static String getTraceId() {
        String traceId = TraceContext.traceId();
        if (StringUtil.hasText(traceId)) {
            return traceId;
        }
        // TODO 调用多次有问题
        return UUID.randomUUID().toString();
    }

}
