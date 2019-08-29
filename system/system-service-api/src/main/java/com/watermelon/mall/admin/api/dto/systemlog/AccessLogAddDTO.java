package com.watermelon.mall.admin.api.dto.systemlog;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class AccessLogAddDTO implements Serializable {

    public static final Integer USER_ID_NULL = 0;

    @NotNull(message = "链路追踪编号不能为空")
    private String traceId;

    @NotNull(message = "用户编号不能为空")
    private Integer userId;

    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    @NotNull(message = "应用名不能为空")
    private String applicationName;

    @NotNull(message = "访问地址不能为空")
    private String uri;

    @NotNull(message = "参数不能为空")
    private String queryString;

    @NotNull(message = "http 方法不能为空")
    private String method;

    @NotNull(message = "userAgent不能为空")
    private String userAgent;

    @NotNull(message = "ip不能为空")
    private String ip;

    @NotNull(message = "请求时间不能为空")
    private Date startTime;

    @NotNull(message = "响应时长不能为空")
    private Integer responseTime;

    @NotNull(message = "错误码不能为空")
    private Integer errorCode;

    @NotNull(message = "错误提示不能为空")
    private String errorMessage;
}
