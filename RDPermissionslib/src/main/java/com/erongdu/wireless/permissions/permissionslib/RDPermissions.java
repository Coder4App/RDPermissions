package com.erongdu.wireless.permissions.permissionslib;

import android.app.Activity;

import com.erongdu.wireless.permissions.permissionslib.proxy.PermissionCallBack;
import com.erongdu.wireless.permissions.permissionslib.utils.Utils;
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

        if (BaseWrapper.cacheMap.get(key) == null || BaseWrapper.cacheMap.get(key).get() == null) {
            return;
        }

        Object      proxyObject = BaseWrapper.cacheMap.get(key).get().getRequestResultProxy().get();
        Object      target      = BaseWrapper.cacheMap.get(key).get().getTarget().get();
        BaseWrapper baseWrapper = BaseWrapper.cacheMap.get(key).get().getWapper().get();

        // 判断代理类 委托类(目标类) 是否存在
        if (proxyObject == null || target == null || baseWrapper == null) {
            return;
        }

        baseWrapper.analysisPermission(permissions, grantResults);

        if (!Utils.isStringArrayEmpty(baseWrapper.getGrantPermission()) && (baseWrapper.getGrantPermission().length == baseWrapper.getPermissions().length)) {
            ((PermissionCallBack) proxyObject).permissionGranted(requestCode, baseWrapper.getGrantPermission(), new int[]{0}, target);
        } else {
            boolean ShowSystemRationale = baseWrapper.shouldShowRationale(baseWrapper.getDeniedPermissions());
            if (ShowSystemRationale) {
                baseWrapper.request();
            } else {
                ((PermissionCallBack) proxyObject).permissionDenied(requestCode, baseWrapper.getDenyPermission(), new int[]{-1}, target);
            }
        }
    }
}