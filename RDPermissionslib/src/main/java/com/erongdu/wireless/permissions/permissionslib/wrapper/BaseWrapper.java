package com.erongdu.wireless.permissions.permissionslib.wrapper;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.erongdu.wireless.permissions.permissionslib.utils.PMConstant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/1 下午3:02
 * <p>
 * Description:
 */
public abstract class BaseWrapper implements Wrapper {
    private static final String                                    PERMISSIONS_PROXY = "$$Proxy";
    public static final  Map<Integer, WeakReference<CacheWrapper>> cacheMap          = new HashMap<>();
    public static final  String                                    TAG               = "RDpermissionBaseWrapper";
    private String[] permissions;
    private boolean  requestWithRationale;
    private int      requestCode;
    //
    private String[] grantPermission;
    private String[] denyPermission;

    @Override
    public Wrapper requestPermission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public Wrapper requestWithRationale() {
        this.requestWithRationale = true;
        return this;
    }

    @Override
    public Wrapper requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public Wrapper requestTargetObject(Object requestTargetObject) {

        if (!cacheMap.containsKey(getActivity().hashCode())) {
            cacheMap.put(getActivity().hashCode(),
                    new WeakReference(
                            new CacheWrapper(getProxy(requestTargetObject.getClass().getSimpleName()), requestTargetObject, this)));
            return this;
        }

        return this;
    }

    @Override
    public void request() {
        if (!cacheMap.containsKey(getActivity().hashCode())) {
            cacheMap.put(getActivity().hashCode(), new WeakReference(new CacheWrapper(this)));
        }
    }

    //************************************/

    /** 获取未获得权限的列表 */
    public String[] getDeniedPermissions() {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(permission);
                Log.e(TAG, "PERMISSION " + permission + " is denied");
            } else {
                Log.e(TAG, "PERMISSION " + permission + " is Grant");
            }
        }

        return deniedList.toArray(new String[deniedList.size()]);
    }

    /** 判断是否需要显示系统权限解释框 */
    public boolean shouldShowRationale(String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                Log.i(TAG, "PERMISSION " + permission + " need to show rationale");
                return true;
            }
            Log.i(TAG, "PERMISSION " + permission + " no need to show rationale");
        }
        return false;
    }

    /** 反射获取apt生成的类 */
    public Object getProxy(String className) {
        String proxyName = PMConstant.packageName + "." + className + PERMISSIONS_PROXY;

        try {
            return Class.forName(proxyName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 分析权限授权结果
     */
    public void analysisPermission(String[] permissions, int[] grantResults) {
        StringBuffer grantSB = new StringBuffer();
        StringBuffer denySB  = new StringBuffer();

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantSB.append(permissions[i]).append(",");
            } else {
                denySB.append(permissions[i]).append(",");
            }
        }

        if (!TextUtils.isEmpty(grantSB)) {
            grantPermission = grantSB.substring(0, grantSB.length()).split(",");
        }

        if (!TextUtils.isEmpty(denySB)) {
            denyPermission = denySB.substring(0, denySB.length()).split(",");
        }
    }

    //***********************************/

    public String[] getPermissions() {
        return permissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String[] getGrantPermission() {
        return grantPermission;
    }

    public String[] getDenyPermission() {
        return denyPermission;
    }

    public class CacheWrapper {
        private WeakReference              requestResultProxy;
        private WeakReference              target;
        private WeakReference<BaseWrapper> wapper;

        public CacheWrapper(Object wapper) {
            this.wapper = new WeakReference(wapper);
        }

        public CacheWrapper(Object requestResultProxy, Object target) {
            this.requestResultProxy = new WeakReference(requestResultProxy);
            this.target = new WeakReference(target);
        }

        public CacheWrapper(Object requestResultProxy, Object target, BaseWrapper wapper) {
            this.requestResultProxy = new WeakReference(requestResultProxy);
            this.target = new WeakReference(target);
            this.wapper = new WeakReference(wapper);
        }

        public WeakReference getRequestResultProxy() {
            return requestResultProxy;
        }

        public void setRequestResultProxy(Object requestResultProxy) {
            this.requestResultProxy = new WeakReference(requestResultProxy);
        }

        public WeakReference getTarget() {
            return target;
        }

        public void setTarget(Object target) {
            this.target = new WeakReference(target);
        }

        public WeakReference<BaseWrapper> getWapper() {
            return wapper;
        }

        public void setWapper(Object wapper) {
            this.wapper = new WeakReference(wapper);
        }
    }
}
