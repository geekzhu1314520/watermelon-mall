package com.watermelon.mall.admin.api.dto.oauth2;

import com.watermelon.common.framework.constant.UserTypeEnum;
import com.watermelon.common.framework.validator.InEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class OAuth2RefreshTokenDTO implements Serializable {

    @NotNull(message = "refreshToken 不能为空")
    private String refreshToken;

    @NotNull(message = "用户类型不能为空")
    @InEnum(value = UserTypeEnum.class, message = "用户类型必须是 {value}")
    private String userType;
}
