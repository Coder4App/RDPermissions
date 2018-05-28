package com.erongdu.wireless.permissions.permissionslib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;

import com.erongdu.wireless.permissions.permissionslib.wrapper.BaseWrapper;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/5/28 下午5:01
 * <p>
 * Description:
 */
public class FragmentWrapper extends BaseWrapper {
    private Fragment fragment;

    public FragmentWrapper(Fragment fragment) {
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

    @SuppressLint("NewApi")
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
