package com.erongdu.wireless.permissions.permissionslib.proxy;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/9 上午10:28
 * <p>
 * Description:
 */
public class RequestPemissionResultProxy implements InvocationHandler {
    private WeakReference<Activity> activityWeakReference ;

    public RequestPemissionResultProxy(Activity activty) {
        this.activityWeakReference = new WeakReference<>(activty);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.d("RequestPemissionssss", method.getName());
        if (method.getName().equals("onRequestPermissionsResult")) {
            Object object = method.invoke(activityWeakReference.get(), args);
            return object;
        }
        return null;
    }
}
