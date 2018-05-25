package com.erongdu.wireless.permissions.permissionslib.utils;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/5/25 下午2:53
 * <p>
 * Description:
 */
public class Utils {
    /**
     * 判断数组是否为空
     */
    public static boolean isStringArrayEmpty(String[] stringArray) {
        if (stringArray == null || stringArray.length == 0) {
            return true;
        } else {
            return false;
        }
    }
}
