package com.watermelon.mall.admin.api.constant;

public enum AdminErrorCodeEnum {

    OAUTH2_NOT_LOGIN(1002001015, "账号未登录"),
    ;

    private Integer code;
    private String message;

    AdminErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
