package com.erongdu.wireless.permissions.permissionslib;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

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
//        String[] deniedList = getDeniedPermissions();
//        if (deniedList.length != 0) {
//            if (shouldShowRationale(deniedList)) {
//                //                ActivityCompat.OnRequestPermissionsResultCallback onRequestPermissionsResultCallback = (ActivityCompat
//                // .OnRequestPermissionsResultCallback)
//                // Proxy.newProxyInstance(activity.getClassLoader(), new Class[]{ActivityCompat.OnRequestPermissionsResultCallback.class}, new
//                //                        RequestPemissionResultProxy(getActivity()));
//                Log.i(TAG, "shouldShowRationale true");
//                ActivityCompat.requestPermissions(getActivity(), deniedList, getRequestCode());
//            } else {
//
//                Log.i(TAG, "shouldShowRationale false");
//                ActivityCompat.requestPermissions(getActivity(), deniedList, getRequestCode());
//            }
//        }
        ActivityCompat.requestPermissions(getActivity(), getPermissions(), getRequestCode());
    }
}