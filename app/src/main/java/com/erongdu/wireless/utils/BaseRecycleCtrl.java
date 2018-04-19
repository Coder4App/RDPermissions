package com.erongdu.wireless.utils;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

import com.erongdu.wireless.permissions.BR;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/4/18 上午10:42
 * <p>
 * Description:
 */
public class BaseRecycleCtrl extends BaseObservable {
    public RecyclerView.Adapter adapter;

    @Bindable
    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }
}
