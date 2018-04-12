package com.erongdu.wireless.permissions.permissionslib.manufacturer;

import android.content.Intent;
import android.os.Build;

import java.util.ArrayList;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/7 下午3:38
 * <p>
 * Description: 获取生产厂家工具类
 */
public class ManufacturerUtils {
    /** Adapted manufacturers */
    public static final String                         MANUFACTURER_XIAOMI = "Xiaomi";
    public static final String                         MANUFACTURER_HUAWEI = "Huawei";
    /** 本机的生产厂家 */
    public static final String                         manufacturer        = Build.MANUFACTURER;
    private static      ArrayList<ManufacturerSetting> cacheList           = new ArrayList<>(1);

    /** 获取对应厂商的权限管理页面的Intent */
    public static Intent getManufacturerManager(String packageName) {
        if (!cacheList.isEmpty()) {
            return cacheList.get(0).getPermissionSettingIntent(packageName);
        }

        ManufacturerSetting setting = null;

        if (manufacturer.equalsIgnoreCase(MANUFACTURER_XIAOMI)) {
            setting = new MFXIAOMI();
        } else if (manufacturer.equalsIgnoreCase(MANUFACTURER_HUAWEI)) {
            setting = new MFHUAWEI();
        } else {
            setting = new MFNative();
        }

        cacheList.add(setting);
        return setting.getPermissionSettingIntent(packageName);
    }
}