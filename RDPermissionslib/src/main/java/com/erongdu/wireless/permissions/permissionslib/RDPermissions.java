package com.erongdu.wireless.permissions.permissionslib;

import android.app.Activity;

import com.erongdu.wireless.permissions.permissionslib.proxy.PermissionCallBack;
import com.erongdu.wireless.permissions.permissionslib.wrapper.BaseWrapper;
import com.erongdu.wireless.permissions.permissionslib.wrapper.Wrapper;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2017/12/22 下午2:23
 * <p>
 * Description:
 */
public class RDPermissions {
    public static Wrapper get(Activity activity) {
        return new ActivityWrapper(activity);
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        onPrivateRequestPermissionsResult(activity, requestCode, permissions, grantResults);
    }

    private static void onPrivateRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        int key = activity.hashCode();

        if (BaseWrapper.cacheMap.get(key).get().getWapper() == null) {
            return;
        }

        Object proxyObject = BaseWrapper.cacheMap.get(key).get().getRequestResultProxy().get();

        if (BaseWrapper.cacheMap.get(key).get().getTarget() == null) {
            return;
        }
        Object target = BaseWrapper.cacheMap.get(key).get().getTarget().get();

        if (BaseWrapper.cacheMap.get(key).get().getWapper() == null) {
            return;
        }
        BaseWrapper baseWrapper = BaseWrapper.cacheMap.get(key).get().getWapper().get();

        if (proxyObject == null || target == null || baseWrapper == null) {
            return;
        }

        baseWrapper.analysisPermission(permissions, grantResults);

        ((PermissionCallBack) proxyObject).permissionGranted(requestCode, baseWrapper.getGrantPermission(), new int[]{0}, target);
        ((PermissionCallBack) proxyObject).permissionDenied(requestCode, baseWrapper.getDenyPermission(), new int[]{-1}, target);
    }
}