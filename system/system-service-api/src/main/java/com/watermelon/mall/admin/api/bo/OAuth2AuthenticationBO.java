package com.watermelon.mall.admin.api.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class OAuth2AuthenticationBO implements Serializable {
    private Integer userId;
    private Integer userType;
}
