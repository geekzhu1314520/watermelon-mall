package com.watermelon.mall.admin.sdk.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestPermissions {

    /**
     * 当有多个标识时，必须全部拥有权限，才可以操作
     *
     * @return 权限标识数组
     */
    String[] value();

}
