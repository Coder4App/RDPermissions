package com.erongdu.wireless.permissions;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.erongdu.wireless.permissions.databinding.ActivityMainBinding;
import com.erongdu.wireless.permissions.permissionslib.RDPermissions;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewCtrl(new MainCtrl(binding));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        RDPermissions.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
        Log.i("MainActivity", "requestCode is " + requestCode + "grantResults" + grantResults.length);
    }
}