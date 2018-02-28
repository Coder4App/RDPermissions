package com.erongdu.wireless.permissions.processor.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/12 下午3:49
 * <p>
 * Description:
 */
public class Utils {
    /**
     * Checks if a CharSequence is empty("") or null
     */
    public static boolean isStringEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * Checks if a CharSequence is not empty("") or null
     */
    public static boolean isStringNotEmpty(CharSequence cs) {
        return !isStringEmpty(cs);
    }

    /**
     * Checks if the specified collection is empty
     */
    public static boolean isCollectionEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Checks if the specified collection is not empty
     */
    public static boolean isCollectionNotEmpty(Collection<?> coll) {
        return !isCollectionEmpty(coll);
    }

    /**
     * Checks if the specified map is empty
     */
    public static boolean isMapEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * Checks if the specified map is not empty
     */
    public static boolean isMapNotEmpty(Map map) {
        return !isMapEmpty(map);
    }
}

