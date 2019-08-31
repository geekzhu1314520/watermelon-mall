package com.watermelon.mall.user.sdk.annotation;

import java.lang.annotation.*;

/**
 * 要求用户登录的注解。通过将该注解添加到 Controller 上，会自动校验用户是否登录。
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresLogin {
}
