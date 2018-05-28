package com.erongdu.wireless.permissions.permissionslib;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.erongdu.wireless.permissions.permissionslib.manufacturer.ManufacturerUtils;
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

    public static Wrapper get(Fragment fragment) {
        return new FragmentWrapper(fragment);
    }

    public static Wrapper get(android.support.v4.app.Fragment supperFragment) {
        return new SupperFragmentWrapper(supperFragment);
    }

    public static void onRequestPermissionsResult(Object context, int requestCode, String[] permissions, int[] grantResults) {
        onPrivateRequestPermissionsResult(context, requestCode, permissions, grantResults);
    }

    private static void onPrivateRequestPermissionsResult(Object context, int requestCode, String[] permissions, int[] grantResults) {
        int key = context.hashCode();

        if (BaseWrapper.cacheMap.get(key) == null) {
            return;
        }

        Object            proxyObject = BaseWrapper.cacheMap.get(key).getRequestResultProxy();
        Object            target      = BaseWrapper.cacheMap.get(key).getTarget().get();
        final BaseWrapper baseWrapper = BaseWrapper.cacheMap.get(key).getWapper().get();

        // 判断代理类 委托类(目标类) 是否存在
        if (proxyObject == null || target == null || baseWrapper == null) {
            return;
        }

        baseWrapper.analysisPermission(permissions, grantResults);

        if (baseWrapper.isPermissonGrantAll()) {
            ((PermissionCallBack) proxyObject).permissionGranted(requestCode, baseWrapper.getGrantPermission(), new int[]{0}, target);
        } else if (!Utils.isStringArrayEmpty(baseWrapper.getDenyPermission())) {
            boolean showSystemRationale = baseWrapper.shouldShowRationale(baseWrapper.getDenyPermission());

            if (!showSystemRationale && baseWrapper.isRequestWithCustomRationale()) {
                ((PermissionCallBack) proxyObject).permissionRationale(requestCode, baseWrapper.getDenyPermission(), new int[]{-1}, target);
            } else if (!showSystemRationale && baseWrapper.isRequestWithRationale()) {
                new AlertDialog.Builder(baseWrapper.getActivity())
                        .setMessage("我们需要您开启权限\n请点击前往设置页面")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                baseWrapper.getActivity().startActivity(ManufacturerUtils.getManufacturerManager(baseWrapper.getActivity().getPackageName()));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            } else {
                ((PermissionCallBack) proxyObject).permissionDenied(requestCode, baseWrapper.getDenyPermission(), new int[]{-1}, target);
            }
        }
    }
}