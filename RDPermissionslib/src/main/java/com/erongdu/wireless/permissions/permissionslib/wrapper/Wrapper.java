package com.erongdu.wireless.permissions.permissionslib.wrapper;

import android.app.Activity;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2017/12/22 上午11:24
 * <p>
 * Description:
 */
public interface Wrapper {
    Activity getActivity();

    Wrapper requestCode(int requestCode);

    /**
     *
     */
    Wrapper requestPermission(String... permissions);

    /**
     * request the permissions with rationale
     */
    Wrapper requestWithRationale();

    Wrapper requestProxyObject(Object requestProxyObject);

    void request();
}
