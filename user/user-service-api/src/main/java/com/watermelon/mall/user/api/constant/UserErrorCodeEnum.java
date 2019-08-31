package com.watermelon.mall.user.api.constant;

public enum  UserErrorCodeEnum {

    ;

    private Integer code;
    private String message;

    UserErrorCodeEnum(Integer code, String message) {
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
