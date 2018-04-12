package com.erongdu.wireless.permissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
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
import com.erongdu.wireless.permissions.annotation.RequestCode;
import com.erongdu.wireless.permissions.databinding.ActivityMainBinding;
import com.erongdu.wireless.permissions.permissionslib.RDPermissions;
import com.erongdu.wireless.utils.ActivityManage;
import com.erongdu.wireless.utils.BaseParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/3 上午10:28
 * <p>
 * Description: viewCtrl {@link MainActivity}
 */
public class MainCtrl {
    private ActivityMainBinding binding;

    public MainCtrl(ActivityMainBinding binding) {
        this.binding = binding;
    }

    public void requsetPermissionClick(View view) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_SMS, Manifest.permission.WRITE_CALENDAR, Manifest.permission
                .WRITE_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE};
        RDPermissions.get(ActivityManage.peek())
                .requestPermission(permissions)
                .requestProxyObject(this)
                .requestCode(101)
                .request();

        //        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //        ActivityManage.peek().startActivity(intent);

        //        WandRPermission();
        //        calendar();
        //                camera();
    }

    public void jumpClick(View view) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        intent.setClassName("com.miui.securitycenter","com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", "com.erongdu.wireless.permissions");
        ActivityManage.peek().startActivity(intent);
    }

    /** 日历查询 */
    @SuppressLint("MissingPermission")
    private void calendar() {
        Cursor cursor = null;

        ContentResolver cr  = ActivityManage.peek().getContentResolver();
        Uri             uri = CalendarContract.Calendars.CONTENT_URI;

        cursor = cr.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID);
            String id = cursor.getString(0);
            Log.i("MainCtrl", id);
        }
    }

    /** 打开相机 */
    private void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        ActivityManage.peek().startActivity(intent);
    }

    /** 文件读写 */
    private void WandRPermission() {
        //创建文件夹路径
        File file = new File(BaseParams.ROOT_PATH);
        file.mkdirs();

        try {
            File fileTest = new File(BaseParams.ROOT_PATH, "test.text");

            if (fileTest.exists())
                fileTest.delete();

            FileOutputStream fileOutputStream = new FileOutputStream(fileTest);
            String           testString       = "test";

            fileOutputStream.write(testString.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PermissionGranted
    public void permissionsGrant(@RequestCode int requestCode, String stringType, @PMArray String[] permissions, int intType,
                                 long longType, float floatType, double doubleType, @GrantResult int[] grantResults, boolean booleanType) {
        //        Toast.makeText(ActivityManage.peek(), "permissionsGrant "+permissions[0], Toast.LENGTH_LONG).show();
    }

    @PermissionDenied
    public void permissionsDeny(@RequestCode int requestCode, @PMArray String[] permissions, @GrantResult int[] grantResults) {
        Toast.makeText(ActivityManage.peek(), "permissionsDeny " + permissions[0], Toast.LENGTH_LONG).show();
    }
}
