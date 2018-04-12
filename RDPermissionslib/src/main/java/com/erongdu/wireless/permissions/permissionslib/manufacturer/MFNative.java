package com.erongdu.wireless.permissions.permissionslib.manufacturer;

import android.content.ComponentName;
import android.content.Intent;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/3/16 上午11:32
 * <p>
 * Description:
 */
public class MFNative implements ManufacturerSetting {
    private final String PAK             = "com.android.settings";
    private final String MANGER_ACTIVITY = "com.android.settings.applications.InstalledAppDetails";

    @Override
    public Intent getPermissionSettingIntent(String packageName) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName componentName = new ComponentName(PAK, MANGER_ACTIVITY);
        intent.setComponent(componentName);
        intent.putExtra("package", packageName);
        return intent;
    }
}
