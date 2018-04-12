package com.erongdu.wireless.permissions.permissionslib.manufacturer;

import android.content.ComponentName;
import android.content.Intent;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/3/15 下午4:45
 * <p>
 * Description:
 */
public class MFHUAWEI implements ManufacturerSetting {
    private final String PAK             = "com.huawei.systemmanager";
    private final String MANGER_ACTIVITY = "com.huawei.permissionmanager.ui.MainActivity";

    @Override
    public Intent getPermissionSettingIntent(String packageName) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName componentName = new ComponentName(PAK, MANGER_ACTIVITY);
        return intent;
    }
}
