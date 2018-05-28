package com.erongdu.wireless.permissions;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.erongdu.wireless.permissions.annotation.GrantResult;
import com.erongdu.wireless.permissions.annotation.PMArray;
import com.erongdu.wireless.permissions.annotation.PermissionDenied;
import com.erongdu.wireless.permissions.annotation.PermissionGranted;
import com.erongdu.wireless.permissions.annotation.PermissionRationale;
import com.erongdu.wireless.permissions.annotation.RequestCode;
import com.erongdu.wireless.permissions.permissionslib.RDPermissions;
import com.erongdu.wireless.utils.ActivityManage;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/5/28 下午4:24
 * <p>
 * Description: ({@link MyFragment})
 */
public class MyFragmentCtrl {
    private Fragment fragment;

    public MyFragmentCtrl(Fragment fragment) {
        this.fragment = fragment;
    }

    public void request(View view) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_SMS, Manifest.permission.WRITE_CALENDAR, Manifest.permission
                .WRITE_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE};
        RDPermissions.get(fragment)
                .requestPermission(permissions)
                .requestTargetObject(this)
                .requestCode(101)
                .requestWithRationale()
                .request();
    }

    @PermissionGranted
    public void permissionsGrant(@RequestCode int requestCode, String stringType, @PMArray String[] permissions, int intType,
                                 long longType, float floatType, double doubleType, @GrantResult int[] grantResults, boolean booleanType) {
        Toast.makeText(ActivityManage.peek(), "permissionsGrant " + permissions[0], Toast.LENGTH_LONG).show();
        //                calendar();
        //                        camera();
    }

    @PermissionDenied
    public void permissionsDeny(@RequestCode int requestCode, @PMArray String[] permissions, @GrantResult int[] grantResults) {
        Toast.makeText(ActivityManage.peek(), "permissionsDeny " + permissions[0], Toast.LENGTH_LONG).show();
    }

    @PermissionRationale
    public void permissionRationale(@RequestCode int requestCode, @PMArray String[] rationalePermissions, @GrantResult int[] grantResults) {
        Toast.makeText(ActivityManage.peek(), "permissionRationale " + rationalePermissions[0], Toast.LENGTH_LONG).show();
    }
}