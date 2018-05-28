package com.erongdu.wireless.permissions;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.erongdu.wireless.permissions.databinding.ActivityFragmentBinding;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/5/28 下午4:20
 * <p>
 * Description:
 */
public class FragmentAct extends AppCompatActivity {
    private ActivityFragmentBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment);
        init();
    }

    private void init() {
        MyFragment fragment = new MyFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content,fragment,MyFragment.class.getSimpleName());
//        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }
}
