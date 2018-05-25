package com.erongdu.wireless.permissions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/5/25 下午3:31
 * <p>
 * Description:
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface PermissionRationale {
}
