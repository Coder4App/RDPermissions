package com.erongdu.wireless.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/3 上午11:18
 * <p>
 * Description:
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityManage.push(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ActivityManage.setTopActivity(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManage.remove(activity);
            }
        });
    }
}
