package com.erongdu.wireless.permissions.permissionslib.manufacturer;

import android.content.ComponentName;
import android.content.Intent;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/3/15 上午10:09
 * <p>
 * Description:
 */
public class MFXIAOMI implements ManufacturerSetting {
    private final String PAK             = "com.miui.securitycenter";
    private final String MANGER_ACTIVITY = "com.miui.permcenter.permissions.AppPermissionsEditorActivity";

    @Override
    public Intent getPermissionSettingIntent(String packageName) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName componentName = new ComponentName(PAK, MANGER_ACTIVITY);
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", packageName);
        return intent;
    }
}