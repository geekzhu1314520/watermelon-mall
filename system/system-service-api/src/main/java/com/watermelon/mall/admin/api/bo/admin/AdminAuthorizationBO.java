package com.watermelon.mall.admin.api.bo.admin;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class AdminAuthorizationBO {

    private Integer id;

    private String username;

    private Set<Integer> roleIds;

}
