package com.erongdu.wireless.permissions.permissionslib.proxy;

import android.support.annotation.NonNull;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/9 上午10:04
 * <p>
 * Description:
 */
public interface RequestPermissionsResultService {
    void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}