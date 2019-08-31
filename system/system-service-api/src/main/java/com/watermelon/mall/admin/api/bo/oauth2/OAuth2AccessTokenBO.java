package com.watermelon.mall.admin.api.bo.oauth2;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class OAuth2AccessTokenBO implements Serializable {
    private String accessToken;
    private String refreshToken;
    private Integer expireIn;
}
