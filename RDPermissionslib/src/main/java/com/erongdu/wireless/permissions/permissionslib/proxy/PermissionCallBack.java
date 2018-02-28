package com.erongdu.wireless.permissions.permissionslib.proxy;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/2/26 上午9:49
 * <p>
 * Description:
 */
public interface PermissionCallBack {
    /** permission grant */
    void permissionGranted(int requestCode, String[] permissions, int[] grantResults, Object proxy);

    /** permission denied */
    void permissionDenied(int requestCode, String[] permissions, int[] grantResults, Object proxy);
}
