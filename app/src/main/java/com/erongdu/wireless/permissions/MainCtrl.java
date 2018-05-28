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
public class MainCtrl extends BaseRecycleCtrl {
    private ActivityMainBinding binding;
    private List<ItemModel> items = new ArrayList();

    public MainCtrl(ActivityMainBinding binding) {
        this.binding = binding;
        init();
    }

    private void init() {
        //        setAdapter(new BaseBindingAdapter<ItemModel, BaseBindingVH>(R.layout.main_item, items) {
        //            @Override
        //            protected void convert(BaseBindingVH helper, ItemModel item) {
        //                helper.getBingding().setVariable(BR.item, item);
        //            }
        //        });
        //
        //        binding.mainRecycle.setLayoutManager(new LinearLayoutManager(ActivityManage.peek()));
        //        StickyItemDecoration itemDecoration = new StickyItemDecoration();
        //        itemDecoration.setListener(new GroupListener() {
        //            @Override
        //            public String getGroupName(int position) {
        //                return items.get(position).getGroup();
        //            }
        //        });
        //        binding.mainRecycle.addItemDecoration(itemDecoration);
        //
        //        ItemModel item0 = new ItemModel("0", "test0");
        //        items.add(item0);
        //        ItemModel item0_1 = new ItemModel("0", "test0_1");
        //        items.add(item0_1);
        //        ItemModel item0_2 = new ItemModel("0", "test0_2");
        //        items.add(item0_2);
        //        ItemModel item1 = new ItemModel("1", "test1");
        //        items.add(item1);
        //        ItemModel item1_1 = new ItemModel("1", "test1_1");
        //        items.add(item1_1);
        //        ItemModel item1_2 = new ItemModel("1", "test1_2");
        //        items.add(item1_2);
        //        ItemModel item2 = new ItemModel("2", "test2");
        //        items.add(item2);
        //        ItemModel item3 = new ItemModel("3", "test3");
        //        items.add(item3);
        //        ItemModel item4 = new ItemModel("4", "test4");
        //        items.add(item4);
        //        ItemModel item5 = new ItemModel("5", "test5");
        //        items.add(item5);
        //        ItemModel item6 = new ItemModel("6", "test6");
        //        items.add(item6);
        //        ItemModel item7 = new ItemModel("7", "test7");
        //        items.add(item7);
        //        ItemModel item8 = new ItemModel("8", "test8");
        //        items.add(item8);
        //        ItemModel item9 = new ItemModel("9", "test9");
        //        items.add(item9);

        //        getAdapter().notifyDataSetChanged();
    }

    public void requsetPermissionClick(View view) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_SMS, Manifest.permission.WRITE_CALENDAR, Manifest.permission
                .WRITE_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE};
        RDPermissions.get(ActivityManage.peek())
                .requestPermission(permissions)
                .requestTargetObject(this)
                .requestCode(101)
                .requestWithRationale()
                .request();

        //        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //        ActivityManage.peek().startActivity(intent);

        //        WandRPermission();
        //        calendar();
        //                camera();
    }

    public void jumpClick(View view) {
        Intent intent = ManufacturerUtils.getManufacturerManager(ActivityManage.peek().getPackageName());
        ActivityManage.peek().startActivity(intent);
    }

    public void jumpFragment(View view) {
        Intent intent = new Intent(ActivityManage.peek(), FragmentAct.class);
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
        Toast.makeText(ActivityManage.peek(), "permissionsGrant " + permissions[0], Toast.LENGTH_LONG).show();
        WandRPermission();
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
