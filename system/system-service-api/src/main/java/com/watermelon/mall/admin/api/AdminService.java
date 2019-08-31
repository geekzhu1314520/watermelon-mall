package com.watermelon.mall.admin.api;

import com.watermelon.mall.admin.api.bo.admin.AdminAuthorizationBO;

import java.util.List;

public interface AdminService {

    /**
     * 判断管理员是否有指定权限
     *
     * @param adminId
     * @param permissions
     * @return
     */
    AdminAuthorizationBO checkPermission(Integer adminId, List<String> permissions);

}
