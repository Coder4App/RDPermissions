package com.erongdu.wireless.permissions;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erongdu.wireless.permissions.databinding.MyFragmentBinding;
import com.erongdu.wireless.permissions.permissionslib.RDPermissions;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/5/28 下午4:24
 * <p>
 * Description:
 */
public class MyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.my_fragment, container, false);
        binding.setViewCtrl(new MyFragmentCtrl(this));
        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RDPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
