package com.watermelon.mall.user.sdk.context;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserSecurityContext {

    private Integer userId;

}
