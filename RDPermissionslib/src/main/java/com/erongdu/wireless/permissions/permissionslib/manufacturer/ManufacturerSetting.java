package com.erongdu.wireless.permissions.permissionslib.manufacturer;

import android.content.Intent;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/3/15 上午10:27
 * <p>
 * Description:
 */
public interface ManufacturerSetting {
    Intent getPermissionSettingIntent(String packageName);
}
