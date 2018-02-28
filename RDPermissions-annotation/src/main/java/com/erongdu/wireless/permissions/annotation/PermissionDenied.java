package com.erongdu.wireless.permissions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/10 下午3:43
 * <p>
 * Description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface PermissionDenied {
}
