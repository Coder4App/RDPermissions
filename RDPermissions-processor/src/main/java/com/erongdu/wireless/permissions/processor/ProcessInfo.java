package com.erongdu.wireless.permissions.processor;

import com.erongdu.wireless.permissions.annotation.GrantResult;
import com.erongdu.wireless.permissions.annotation.PMArray;
import com.erongdu.wireless.permissions.annotation.RequestCode;
import com.erongdu.wireless.permissions.processor.utils.Consts;
import com.erongdu.wireless.permissions.processor.utils.Logger;
import com.erongdu.wireless.permissions.processor.utils.Utils;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.TypeName;

import java.util.LinkedHashMap;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/14 下午5:21
 * <p>
 * Description:处理注解，存储必要的信息
 */
public class ProcessInfo {
    /** 存储被PermissionDenied注解的方法的参数信息 */
    private LinkedHashMap<String, Entity> paramsDenyMap;
    /** 存储被PermissionGranted注解的方法的参数信息 */
    private LinkedHashMap<String, Entity> paramsGrantMap;
    /** 存储被PermissionRationale注解的方法的参数信息 */
    private LinkedHashMap<String, Entity> paramsRationaleMap;
    /** 被PermissionGranted注解的方法的方法名 */
    private String                        methodGrantName;
    /** 被PermissionDenied注解的方法的方法名 */
    private String                        methodDenyName;
    /** 被PermissionRationale注解的方法的方法名 */
    private String                        methodRationaleName;

    public ProcessInfo() {
        paramsDenyMap = new LinkedHashMap<>();
        paramsGrantMap = new LinkedHashMap<>();
        paramsRationaleMap = new LinkedHashMap<>();
    }

    /**
     * grant方法解析
     */
    public void resolveGrantMethod(Logger logger, Element element, Elements elementUtils, Types typeUtils) {

        logger.info(" >>> Start resolve child element which parent element is annotation by PermissionGranted. <<<");
        //        TypeMirror type_RequestCode = elementUtils.getTypeElement(Consts.ANNOTATION_TYPE_REQUESTCODE).asType();
        //        TypeMirror type_PMArray     = elementUtils.getTypeElement(Consts.ANNOTATION_TYPE_PMARRAY).asType();
        //        TypeMirror type_GrantResult = elementUtils.getTypeElement(Consts.ANNOTATION_TYPE_GRANTREULT).asType();

        //获取方法的方法名
        methodGrantName = element.getSimpleName().toString();

        //获取该方法下的所有传参
        List<? extends VariableElement> parameters = ((ExecutableElement) element).getParameters();
        if (Utils.isCollectionNotEmpty(parameters)) {

            //遍历所有的参数
            for (Element parameter : parameters) {
                Entity entity = new Entity(parameter);
                entity.setParameterName(parameter.getSimpleName().toString());
                entity.setParameterType(parameter.asType().toString());
                checkAnnotationByLib(parameter, entity);
                paramsGrantMap.put(entity.getKeyWord(), entity);

                //                logger.info(" >>> method now is " + methodGrantName
                //                        + "\n is annotation " + entity.isPMLibAnnotation()
                //                        + "\n this is parameter " + parameter.asType().toString()
                //                        + "\n getArrayTypeName " + ArrayTypeName.get(parameter.asType())
                //                        +"\n TypeName is " + entity.getTypeName()
                //                        + "\n parameter name is " + parameter.getSimpleName()
                //                        + "\n parameter type name is " + parameter.asType().toString() + " / " + parameter.asType()
                //                        + "\n " + parameter.asType().getKind()
                //                                        + "\n parameter annotation name is " + annotationList.get(0).getAnnotationType().asElement
                // ().getSimpleName()
                //                                        + "\n " + typeUtils.isSameType(annotationList.get(0).getAnnotationType().asElement().asType
                // (), type_RequestCode)
                //                        + "\n parameter type is integer " + typeUtils.isAssignable(type_Integer, parameter.asType()) + " / " +
                // type_Integer
                //                        .toString()
                //                        + "\n parameter type is integer[] " + typeUtils.isSubtype(type_Array_Integer, parameter.asType()) + " / " +
                //                        type_Array_Integer.toString()
                //                        + "\n parameter type is String[] " + typeUtils.isSameType(parameter.asType(), type_Array_String) + " / " +
                //                        type_Array_String.toString()
                //                        + "\n" + parameter.asType().getClass().getSimpleName()
                //                        + " / Integer Array TypeMirror name is " +
                //                        type_Array_Integer.getClass().getSimpleName() + " / is Array " + (parameter.asType().getKind() == TypeKind
                // .ARRAY)
                //                        + "\n" + typeUtils.capture(type_Array_Integer).toString() + " / " + parameter.asType().toString()
                //                        + "\n <<<");
            }
        }
    }

    /**
     * deny方法解析
     */
    public void resolveDeniedMethod(Logger logger, Element element) {

        logger.info(" >>> Start resolve child element which parent element is annotation by PermissionDenied. <<<");

        //获取方法的方法名
        methodDenyName = element.getSimpleName().toString();
        //获取该方法下的所有形参
        List<? extends VariableElement> parameters = ((ExecutableElement) element).getParameters();
        if (Utils.isCollectionNotEmpty(parameters)) {

            //遍历所有的参数
            for (Element parameter : parameters) {
                Entity entity = new Entity(parameter);
                entity.setParameterName(parameter.getSimpleName().toString());
                entity.setParameterType(parameter.asType().toString());
                checkAnnotationByLib(parameter, entity);
                paramsDenyMap.put(entity.getKeyWord(), entity);
            }
        }
    }

    public void resolveRationaleMethod(Logger logger, Element element) {
        logger.info(" >>> Start resolve child element which parent element is annotation by PermissionRationale. <<<");

        //获取方法的方法名
        methodRationaleName = element.getSimpleName().toString();
        //获取该方法下的所有形参
        List<? extends  VariableElement> parameters = ((ExecutableElement) element).getParameters();
        if (Utils.isCollectionNotEmpty(parameters)){
            //遍历所有参数
            for(Element parameter : parameters){
                Entity entity = new Entity(parameter);
                entity.setParameterName(parameter.getSimpleName().toString());
                entity.setParameterType(parameter.asType().toString());
                checkAnnotationByLib(parameter,entity);
                paramsRationaleMap.put(entity.getKeyWord(),entity);
            }
        }
    }

    /**
     * 是否被权限库的指定注解 注解
     */
    private void checkAnnotationByLib(Element parameter, Entity entity) {

        if (parameter.getAnnotation(RequestCode.class) != null && parameter.asType().toString().equals(Consts.TYPEMIRROR_INT)) {
            //被@RequestCode注解
            entity.setPMLibAnnotation(true);
            entity.setTypeName(TypeName.get(parameter.asType()));
            entity.setKeyWord(Consts.ANNOTATION_MAPPING_REQUESTCODE);
            return;
        } else if (parameter.getAnnotation(PMArray.class) != null && parameter.asType().toString().equals(Consts.TYPEMIRROR_ARRAY_STRING)) {
            //被@PMArray
            entity.setPMLibAnnotation(true);
            entity.setTypeName(TypeName.get(parameter.asType()));
            entity.setKeyWord(Consts.ANNOTATION_MAPPING_PERMISSIONS);

            return;
        } else if (parameter.getAnnotation(GrantResult.class) != null && parameter.asType().toString().equals(Consts.TYPEMIRROR_ARRAY_INT)) {
            //被@GrantResult注解
            entity.setPMLibAnnotation(true);
            entity.setTypeName(ArrayTypeName.get(parameter.asType()));
            entity.setKeyWord(Consts.ANNOTATION_MAPPING_GRANTRESULTS);
            return;
        } else {
            entity.setKeyWord(entity.getParameterName() + entity.getTypeName());
        }
    }

    public LinkedHashMap<String, Entity> getParamsDenyMap() {
        return paramsDenyMap;
    }

    public LinkedHashMap<String, Entity> getParamsGrantMap() {
        return paramsGrantMap;
    }

    public LinkedHashMap<String, Entity> getParamsRationaleMap() {
        return paramsRationaleMap;
    }

    public String getMethodGrantName() {
        return methodGrantName;
    }

    public String getMethodDenyName() {
        return methodDenyName;
    }

    public String getMethodRationaleName() {
        return methodRationaleName;
    }
}