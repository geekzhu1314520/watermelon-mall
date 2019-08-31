package com.watermelon.mall.user.sdk.interceptor;

import com.watermelon.common.framework.constant.UserTypeEnum;
import com.watermelon.common.framework.exception.ServiceException;
import com.watermelon.common.framework.util.HttpUtil;
import com.watermelon.common.framework.util.MallUtil;
import com.watermelon.mall.admin.api.OAuth2Service;
import com.watermelon.mall.admin.api.bo.oauth2.OAuth2AuthenticationBO;
import com.watermelon.mall.admin.api.constant.AdminErrorCodeEnum;
import com.watermelon.mall.admin.api.dto.oauth2.OAuth2GetTokenDTO;
import com.watermelon.mall.user.sdk.annotation.RequiresLogin;
import com.watermelon.mall.user.sdk.context.UserSecurityContext;
import com.watermelon.mall.user.sdk.context.UserSecurityContextHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserSecurityInterceptor extends HandlerInterceptorAdapter {

    @Reference(validation = "true", consumer = "${dubbo.consumer.OAuth2Service.version:1.0.0}")
    private OAuth2Service oAuth2Service;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设置当前用户的访问类型
        MallUtil.setUserType(request, UserTypeEnum.USER.getValue());

        // 根据 accessToken 获取认证信息
        String accessToken = HttpUtil.obtainAuthorization(request);
        OAuth2AuthenticationBO authentication = null;
        ServiceException serviceException = null;
        try {
            authentication = oAuth2Service.getAuthentication(
                    new OAuth2GetTokenDTO().setUserType(UserTypeEnum.USER.getValue()).setAccessToken(accessToken)
            );
        } catch (ServiceException e) {
            serviceException = e;
        }

        // 进行鉴权
        HandlerMethod method = (HandlerMethod) handler;
        boolean requiresLogin = method.hasMethodAnnotation(RequiresLogin.class);
        if (requiresLogin) {
            if (serviceException != null) {
                throw serviceException;
            }
            if (authentication == null) {
                throw new ServiceException(AdminErrorCodeEnum.OAUTH2_NOT_LOGIN.getCode(), AdminErrorCodeEnum.OAUTH2_NOT_LOGIN.getMessage());
            }
        }

        // 鉴权完毕，初始化context上下文
        UserSecurityContext context = new UserSecurityContext();
        UserSecurityContextHolder.setContext(context);
        if (authentication != null) {
            context.setUserId(authentication.getUserId());
            MallUtil.setUserId(request, authentication.getUserId());
        }

        //返回成功
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserSecurityContextHolder.clear();
    }
}
