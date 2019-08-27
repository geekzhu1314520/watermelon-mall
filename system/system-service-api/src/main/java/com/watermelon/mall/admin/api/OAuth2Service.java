package com.watermelon.mall.admin.api;

import com.watermelon.mall.admin.api.bo.OAuth2AccessTokenBO;
import com.watermelon.mall.admin.api.bo.OAuth2AuthenticationBO;
import com.watermelon.mall.admin.api.dto.oauth2.OAuth2CreateTokenDTO;
import com.watermelon.mall.admin.api.dto.oauth2.OAuth2GetTokenDTO;
import com.watermelon.mall.admin.api.dto.oauth2.OAuth2RefreshTokenDTO;
import com.watermelon.mall.admin.api.dto.oauth2.OAuth2RemoveTokenByUserDTO;

/**
 * OAuth2 服务接口
 */
public interface OAuth2Service {

    /**
     * 根据身份信息，创建 accessToken 信息
     *
     * @param oAuth2CreateTokenDTO
     * @return accessToken 信息
     */
    OAuth2AccessTokenBO createToken(OAuth2CreateTokenDTO oAuth2CreateTokenDTO);

    /**
     * 基于用户移除 accessToken
     *
     * @param oAuth2RemoveTokenDTO
     */
    void removeToken(OAuth2RemoveTokenByUserDTO oAuth2RemoveTokenDTO);

    /**
     * 刷新令牌，获取新的 accessToken 信息
     *
     * @param auth2RefreshTokenDTO
     * @return accessToken 信息
     */
    OAuth2AccessTokenBO refreshToken(OAuth2RefreshTokenDTO auth2RefreshTokenDTO);

    /**
     * 通过 accessToken 获取身份信息
     *
     * @param oAuth2GetTokenDTO
     * @return 身份信息
     */
    OAuth2AuthenticationBO getAuthentication(OAuth2GetTokenDTO oAuth2GetTokenDTO);
}
