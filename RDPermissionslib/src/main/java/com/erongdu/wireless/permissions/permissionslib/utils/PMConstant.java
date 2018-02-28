package com.erongdu.wireless.permissions.permissionslib.utils;

import android.Manifest;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2017/12/22 下午5:04
 * <p>
 * Description:
 */
public class PMConstant {
    public  static final String packageName = "com.erongdu.wireless.permissions";
    public static final String[] CAMERA;

    static {
        CAMERA = new String[]{
                Manifest.permission.CAMERA
        };
    }
}
