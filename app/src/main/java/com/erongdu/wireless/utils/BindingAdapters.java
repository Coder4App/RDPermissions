package com.erongdu.wireless.utils;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/4/17 下午5:22
 * <p>
 * Description:
 */
public class BindingAdapters {
    @BindingAdapter({"recyclerAdapter"})
    public static void recyclerAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return;
        }
        if (view.getAdapter() == null) {
            view.setAdapter(adapter);
        } else {
            view.swapAdapter(adapter, false);
        }
    }
}
