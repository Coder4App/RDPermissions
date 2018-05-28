package com.erongdu.wireless.permissions.permissionslib;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.erongdu.wireless.permissions.permissionslib.wrapper.BaseWrapper;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/5/28 下午5:11
 * <p>
 * Description:
 */
public class SupperFragmentWrapper extends BaseWrapper {
    private Fragment fragment;

    public SupperFragmentWrapper(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Activity getActivity() {
        return fragment.getActivity();
    }

    @Override
    public Object getContext() {
        return fragment;
    }

    @Override
    public void request() {
        super.request();
        if (shouldShowRationale(getPermissions())) {
            fragment.requestPermissions(getPermissions(), getRequestCode());
        } else {
            //第一申请权限 shouldShowRationale 默认都是false 所以添加这个
            fragment.requestPermissions(getPermissions(), getRequestCode());
        }
    }
}
