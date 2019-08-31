package com.watermelon.mall.user.sdk.context;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserSecurityContextHolder {

    private static final ThreadLocal<UserSecurityContext> SECURITY_CONTEXT = new ThreadLocal<>();

    public static void setContext(UserSecurityContext context) {
        SECURITY_CONTEXT.set(context);
    }

    public static UserSecurityContext getContext() {
        UserSecurityContext context = SECURITY_CONTEXT.get();
        if (context == null) {
            context = new UserSecurityContext();
            SECURITY_CONTEXT.set(context);
        }
        return context;
    }

    public static void clear() {
        SECURITY_CONTEXT.remove();
    }

}
