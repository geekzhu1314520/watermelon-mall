package com.watermelon.mall.admin.sdk.interceptor;

import com.watermelon.common.framework.constant.UserTypeEnum;
import com.watermelon.common.framework.exception.ServiceException;
import com.watermelon.common.framework.util.HttpUtil;
import com.watermelon.common.framework.util.MallUtil;
import com.watermelon.mall.admin.api.AdminService;
import com.watermelon.mall.admin.api.OAuth2Service;
import com.watermelon.mall.admin.api.bo.admin.AdminAuthorizationBO;
import com.watermelon.mall.admin.api.bo.oauth2.OAuth2AuthenticationBO;
import com.watermelon.mall.admin.api.constant.AdminErrorCodeEnum;
import com.watermelon.mall.admin.api.dto.oauth2.OAuth2GetTokenDTO;
import com.watermelon.mall.admin.sdk.annotation.RequestPermissions;
import com.watermelon.mall.admin.sdk.context.AdminSecurityContext;
import com.watermelon.mall.admin.sdk.context.AdminSecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Set;

@Component
public class AdminSecurityInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private OAuth2Service oAuth2Service;
    @Autowired
    private AdminService adminService;

    @Value("${admins.security.ignore_urls:#{null}}")
    private Set<String> ignoreUrls;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置当前访问的用户类型
        MallUtil.setUserType(request, UserTypeEnum.ADMIN.getValue());

        //根据accessToken获取认证信息
        String accessToken = HttpUtil.obtainAuthorization(request);
        OAuth2AuthenticationBO authenticationBO = null;
        ServiceException serviceException = null;
        if (StringUtils.hasText(accessToken)) {
            try {
                authenticationBO = oAuth2Service.getAuthentication(new OAuth2GetTokenDTO()
                        .setAccessToken(accessToken).setUserType(UserTypeEnum.ADMIN.getValue()));
            } catch (ServiceException ex) {
                serviceException = ex;
            }
        }

        //进行鉴权
        String url = request.getRequestURI();
        boolean needAuthentication = ignoreUrls == null || !ignoreUrls.contains(url);
        AdminAuthorizationBO adminAuthorization = null;
        if (needAuthentication) {
            if (serviceException != null) {
                throw serviceException;
            }
            if (authenticationBO == null) {
                throw new ServiceException(AdminErrorCodeEnum.OAUTH2_NOT_LOGIN.getCode(), AdminErrorCodeEnum.OAUTH2_NOT_LOGIN.getMessage());
            }
            adminAuthorization = checkPermission(handler, authenticationBO);
        }

        //鉴权完毕，初始化Context上下文
        AdminSecurityContext context = new AdminSecurityContext();
        AdminSecurityContextHolder.setContext(context);
        if (authenticationBO != null) {
            context.setAdminId(authenticationBO.getUserId());
            MallUtil.setUserId(request, authenticationBO.getUserId());
            if (adminAuthorization != null) {
                context.setUsername(adminAuthorization.getUsername());
                context.setRoleIds(adminAuthorization.getRoleIds());
            }
        }

        //返回成功
        return super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    private AdminAuthorizationBO checkPermission(Object handler, OAuth2AuthenticationBO authenticationBO) {
        Assert.isTrue(handler instanceof HandlerMethod, "handler必须是 HandlerMethod 类型");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequestPermissions requestPermissions = handlerMethod.getMethodAnnotation(RequestPermissions.class);
        return adminService.checkPermission(authenticationBO.getUserId(),
                requestPermissions != null ? Arrays.asList(requestPermissions.value()) : null);
    }

}
