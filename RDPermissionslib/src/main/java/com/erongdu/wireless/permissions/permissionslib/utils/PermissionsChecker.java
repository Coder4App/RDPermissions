package com.erongdu.wireless.permissions.permissionslib.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2017/12/22 下午5:17
 * <p>
 * Description: 权限检查类
 */
public class PermissionsChecker {
    public static boolean checkPermissionGrant(Context context, String permissions) {
        return ContextCompat.checkSelfPermission(context, permissions) == 0;
    }
}
