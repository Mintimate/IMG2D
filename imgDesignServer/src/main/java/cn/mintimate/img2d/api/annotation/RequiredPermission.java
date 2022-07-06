package cn.mintimate.img2d.api.annotation;

import java.lang.annotation.*;

/**
 * Desc 与拦截器结合使用 验证权限
 *
 * @author Mintimate
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredPermission {
    boolean admin() default false;
}
