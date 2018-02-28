package com.erongdu.wireless.permissions.processor.utils;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/2/1 下午6:10
 * <p>
 * Description:
 */
public class TypeUtils {
    public static String typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();
        if (typeMirror.getKind().ordinal() == TypeKind.ARRAY.ordinal()) {
            return null;
        }

        switch (typeMirror.toString()) {
            case Consts.BYTE:
                return "-1";
            case Consts.PRIMITIVE_BYTE:
                return "-1";
            case Consts.SHORT:
                return "-1.0";
            case Consts.PRIMITIVE_SHORT:
                return "-1";
            case Consts.INTEGER:
                return "-1";
            case Consts.PRIMITIVE_INTEGER:
                return "-1";
            case Consts.LONG:
                return "-1";
            case Consts.PRIMITIVE_LONG:
                return "-1";
            case Consts.FLOAT:
                return "-1";
            case Consts.PRIMITIVE_FLOAT:
                return "-1";
            case Consts.DOUBEL:
                return "-1";
            case Consts.PRIMITIVE_DOUBEL:
                return "-1";
            case Consts.BOOLEAN:
                return "false";
            case Consts.PRIMITIVE_BOOLEAN:
                return "false";
            case Consts.STRING:
                return null;
            default:
                return null;
        }
    }
}
