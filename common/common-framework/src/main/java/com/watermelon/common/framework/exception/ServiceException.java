package com.watermelon.common.framework.exception;

/**
 * 服务异常
 * 参考：https://www.kancloud.cn/onebase/ob/484204
 * 一共10位，分4段
 * <p>
 * 第1段，1位，类型
 * 1 - 业务级别异常
 * 2 - 系统级别异常
 * <p>
 * 第2段，3位，系统类型
 * 001 - 用户系统
 * 002 - 管理员系统
 * 003 - 商品系统
 * 004 - 支付系统
 * 005 -
 * 006 - 营销系统
 * 007 -
 * 008 - 订单系统
 * <p>
 * 第3段，3位，模块
 * 每个系统可以有多个模块，一个模块对应一段，以用户系统为例：
 * 001 - OAuth2模块
 * 002 - 用户模块
 * <p>
 * 第4段，3位，错误码
 * 每个模块自增
 */
public final class ServiceException extends RuntimeException {

    private final Integer code;

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
