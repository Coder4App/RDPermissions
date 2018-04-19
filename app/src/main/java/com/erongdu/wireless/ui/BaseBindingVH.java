package com.erongdu.wireless.ui;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.erongdu.wireless.permissions.R;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/4/17 下午5:30
 * <p>
 * Description:
 */
public class BaseBindingVH extends BaseViewHolder{

    public BaseBindingVH(View view) {
        super(view);
    }

    public ViewDataBinding getBingding(){
       return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
    }
}
