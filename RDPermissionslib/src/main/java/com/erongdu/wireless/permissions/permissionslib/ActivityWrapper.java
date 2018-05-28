package com.erongdu.wireless.permissions.permissionslib;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import com.erongdu.wireless.permissions.permissionslib.wrapper.BaseWrapper;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2017/12/22 下午5:10
 * <p>
 * Description:
 */
public class ActivityWrapper extends BaseWrapper {
    private Activity activity;

    public ActivityWrapper(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void request() {
        super.request();

        if (shouldShowRationale(getPermissions())) {
            ActivityCompat.requestPermissions(getActivity(), getPermissions(), getRequestCode());
        } else {
            //第一申请权限 shouldShowRationale 默认都是false 所以添加这个
            ActivityCompat.requestPermissions(getActivity(), getPermissions(), getRequestCode());
        }
    }
}