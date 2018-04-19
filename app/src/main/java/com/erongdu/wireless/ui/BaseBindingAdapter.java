package com.erongdu.wireless.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.erongdu.wireless.permissions.R;

import java.util.List;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/4/17 下午5:41
 * <p>
 * Description:
 */
public abstract class BaseBindingAdapter<k, T extends BaseViewHolder> extends BaseQuickAdapter<k, T> {
    public BaseBindingAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    public BaseBindingAdapter(@Nullable List data) {
        super(data);
    }

    public BaseBindingAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }
}
