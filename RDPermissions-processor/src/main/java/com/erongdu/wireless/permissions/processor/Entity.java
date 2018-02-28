package com.erongdu.wireless.permissions.processor;

import com.erongdu.wireless.permissions.processor.utils.TypeUtils;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/14 下午7:37
 * <p>
 * Description:
 */
public class Entity {
    private Element  element;
    /** 参数名 */
    private String   parameterName;
    /** 参数类型名 */
    private String   parameterType;
    /** 是否是被权限库限定的注解 注解的参数 */
    private boolean  PMLibAnnotation;
    /** 关键词 用于当作key值用 */
    private String   keyWord;
    /** 类的TypeName */
    private TypeName typeName;

    public Entity(Element element) {
        this.element = element;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public boolean isPMLibAnnotation() {
        return PMLibAnnotation;
    }

    public void setPMLibAnnotation(boolean PMLibAnnotation) {
        this.PMLibAnnotation = PMLibAnnotation;
    }


    public String getDefaultValus() {
        return TypeUtils.typeExchange(element);
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeName typeName) {
        this.typeName = typeName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
