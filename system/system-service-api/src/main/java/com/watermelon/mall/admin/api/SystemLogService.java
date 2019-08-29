package com.watermelon.mall.admin.api;

import com.watermelon.mall.admin.api.dto.systemlog.AccessLogAddDTO;
import com.watermelon.mall.admin.api.dto.systemlog.ExceptionLogAddDTO;

public interface SystemLogService {

    /**
     * 增加系统异常
     *
     * @param exceptionLogAddDTO
     */
    void addExceptionLog(ExceptionLogAddDTO exceptionLogAddDTO);

    /**
     * 增加访问日志
     *
     * @param accessLogAddDTO
     */
    void addAccessLog(AccessLogAddDTO accessLogAddDTO);

}
