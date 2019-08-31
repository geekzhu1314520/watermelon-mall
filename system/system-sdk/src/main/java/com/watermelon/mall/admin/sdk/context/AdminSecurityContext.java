package com.watermelon.mall.admin.sdk.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class AdminSecurityContext {

    private Integer adminId;

    private String username;

    private Set<Integer> roleIds;

}
