package com.watermelon.mall.admin.sdk.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class AdminSecurityContextHolder {

    private static final ThreadLocal<AdminSecurityContext> SECURITY_CONTEXT = new ThreadLocal<>();

    public static void setContext(AdminSecurityContext context) {
        SECURITY_CONTEXT.set(context);
    }

    public static AdminSecurityContext getContext() {
        AdminSecurityContext context = SECURITY_CONTEXT.get();
        if (context == null) {
            context = new AdminSecurityContext();
            SECURITY_CONTEXT.set(context);
        }
        return context;
    }

    public static void clear() {
        SECURITY_CONTEXT.remove();
    }

}
