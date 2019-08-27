package com.watermelon.mall.admin.api.dto.oauth2;

import com.watermelon.common.framework.constant.UserTypeEnum;
import com.watermelon.common.framework.validator.InEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class OAuth2CreateTokenDTO implements Serializable {

    @NotNull(message = "用户编号不能为空")
    private Integer userId;

    @NotNull(message = "用户类型不能为空")
    @InEnum(value = UserTypeEnum.class, message = "用户类型必须是 {value}")
    private Integer userType;

}
