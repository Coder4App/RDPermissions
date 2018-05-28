package com.erongdu.wireless.permissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.erongdu.wireless.permissions.annotation.GrantResult;
import com.erongdu.wireless.permissions.annotation.PMArray;
import com.erongdu.wireless.permissions.annotation.PermissionDenied;
import com.erongdu.wireless.permissions.annotation.PermissionGranted;
import com.erongdu.wireless.permissions.annotation.PermissionRationale;
import com.erongdu.wireless.permissions.annotation.RequestCode;
import com.erongdu.wireless.permissions.databinding.ActivityMainBinding;
import com.erongdu.wireless.permissions.permissionslib.RDPermissions;
import com.erongdu.wireless.permissions.permissionslib.manufacturer.ManufacturerUtils;
import com.erongdu.wireless.utils.ActivityManage;
import com.erongdu.wireless.utils.BaseParams;
import com.erongdu.wireless.utils.BaseRecycleCtrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/3 上午10:28
 * <p>
 * Description: viewCtrl {@link MainActivity}
 */
public class TestCtrl extends BaseRecycleCtrl {

    public TestCtrl() {
    }



//    @PermissionGranted
//    public void permissionsGrant(@RequestCode int requestCode, String stringType, @PMArray String[] permissions, int intType,
//                                 long longType, float floatType, double doubleType, @GrantResult int[] grantResults, boolean booleanType) {
//        Toast.makeText(ActivityManage.peek(), "permissionsGrant " + permissions[0], Toast.LENGTH_LONG).show();
//    }

    @PermissionDenied
    public void permissionsDeny(@RequestCode int requestCode, @PMArray String[] permissions, @GrantResult int[] grantResults) {
        Toast.makeText(ActivityManage.peek(), "permissionsDeny " + permissions[0], Toast.LENGTH_LONG).show();
    }

//    @PermissionRationale
//    public void permissionRationale(@RequestCode int requestCode, @PMArray String[] rationalePermissions, @GrantResult int[] grantResults) {
//        Toast.makeText(ActivityManage.peek(), "permissionRationale " + rationalePermissions[0], Toast.LENGTH_LONG).show();
//    }
}
