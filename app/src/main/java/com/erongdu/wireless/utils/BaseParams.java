package com.erongdu.wireless.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by chenwei on 2017/5/5.
 * Description:公用的参数 需要脚本配置的参数
 */
public class BaseParams {

    //********************  无需脚本修改的参数  *******************/
    /** 根路径 */
    public static final String ROOT_PATH  = getSDPath() + "/RDPermission";
    /** 照片文件文件保存路径 */
    public static final String PHOTO_PATH = ROOT_PATH + "/photo";

    /**
     * 获取SD卡的根目录
     */
    public static String getSDPath() {
        File sdDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            // 获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
        }
        if (sdDir == null) {
            return "";
        } else {
            return sdDir.toString();
        }
    }
}
