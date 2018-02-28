package com.erongdu.wireless.permissions.processor;

import com.erongdu.wireless.permissions.annotation.PermissionDenied;
import com.erongdu.wireless.permissions.annotation.PermissionGranted;
import com.erongdu.wireless.permissions.processor.utils.Consts;
import com.erongdu.wireless.permissions.processor.utils.Logger;
import com.erongdu.wireless.permissions.processor.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/11 下午8:06
 * <p>
 * Description:注解处理器
 * 1 @AutoService AutoService注解处理器是Google开发的，用来生成META-INF/services/javax.annotation.processing.Processor文件的
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({Consts.ANNOTATION_TYPE_PERMISSIONGRANTED,
        Consts.ANNOTATION_TYPE_PERMISSIONDENIED,
        Consts.ANNOTATION_TYPE_GRANTREULT,
        Consts.ANNOTATION_TYPE_PMARRAY,
        Consts.ANNOTATION_TYPE_REQUESTCODE})
public class AnnotationProcessor extends AbstractProcessor {
    private Map<String, ProcessInfo> map = new HashMap<>();
    private Types    typeUtils;
    private Elements elementUtils;
    private Filer    mFiler;
    /* 用来处理错误日志 */
    private Logger   logger;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mFiler = processingEnv.getFiler(); //Generate class.
        typeUtils = processingEnv.getTypeUtils(); //Get type utils.
        elementUtils = processingEnv.getElementUtils(); // Get class meta.

        logger = new Logger(processingEnv.getMessager());

        logger.info(" >>> AnnotationProcessor init. <<< ");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (Utils.isCollectionNotEmpty(annotations)) {
            try {
                logger.info(" >>> Found  annotation <<<");
                //注意这里必须清空，因为注解处理器会扫多遍，如果不clear(),会重复的生产之前的需要生成的类，造成错误
                map.clear();

                //interface of proxy
                TypeMirror type_PermissionCallBack = elementUtils.getTypeElement(Consts.INTERFACE_PERMISSIONCALLBACK).asType();

                prasePermissionGrantedMethod(roundEnv);
                prasePermissionDeniedMethod(roundEnv);

                for (String qualifiedName : map.keySet()) {
                    logger.info(" >>> Generate code " + qualifiedName);
                    MethodSpec grantMethodSpec = generateGrantMethod(qualifiedName);
                    MethodSpec denyMethodSpec  = generateDenyMethod(qualifiedName);

                    // generate java code
                    String generateClassName = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1, qualifiedName.length()) + Consts.SEPARATOR + "Proxy";
                    TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(generateClassName)
                            .addJavadoc(Consts.WARNING_TIPS)
                            .addModifiers(Modifier.PUBLIC)
                            .addSuperinterface(TypeName.get(type_PermissionCallBack));

                    if (grantMethodSpec != null) {
                        typeBuilder.addMethod(grantMethodSpec);
                        logger.info(" >>> add method grantMethod");
                    }
                    if (denyMethodSpec != null) {
                        typeBuilder.addMethod(denyMethodSpec);
                        logger.info(" >>> add method denyMethod");
                    }

                    JavaFile.builder(Consts.PACKAGE_OF_GENERATE_FILE, typeBuilder.build())
                            .build().writeTo(mFiler);
                }
            } catch (Exception e) {
                logger.error(e);
            }

            return true;
        }

        return false;
    }

    /**
     * 处理被PermissionGranted注解方法
     */
    private void prasePermissionGrantedMethod(RoundEnvironment roundEnv) {
        Set<? extends Element> grantedElements = roundEnv.getElementsAnnotatedWith(PermissionGranted.class);
        if (Utils.isCollectionNotEmpty(grantedElements)) {
            logger.info(" >>> Found annotation PermissionGranted,size is " + grantedElements.size() + " <<<");

            for (Element element : grantedElements) {
                //PermissionGranted 必须注解在方法上，虽然注解有限定(ElementType.METHOD)，但是这边还是做了一层处理,判断是否注解在方法上
                if (element.asType().getKind() == TypeKind.EXECUTABLE) {
                    //获取parent Element
                    TypeElement parentElement = (TypeElement) element.getEnclosingElement();
                    String      qualifiedName = parentElement.getQualifiedName().toString();
                    ProcessInfo info          = map.get(qualifiedName);
                    if (info == null) {
                        info = new ProcessInfo();
                        map.put(qualifiedName, info);
                    }

                    if (Utils.isMapNotEmpty(info.getParamsGrantMap())) {
                        throw new RuntimeException("RDPermission::Compiler >>> there is already have method which is annotated by @PermissionGranted");
                    }

                    //扫描方法参数
                    info.resolveGrantMethod(logger, element, elementUtils, typeUtils);
                }
            }
        }
    }

    /**
     * 处理被PermissionDenied注解方法
     */
    private void prasePermissionDeniedMethod(RoundEnvironment roundEnv) {
        Set<? extends Element> deniedElements = roundEnv.getElementsAnnotatedWith(PermissionDenied.class);
        if (Utils.isCollectionNotEmpty(deniedElements)) {
            logger.info(" >>> Found annotation PermissionDenied,size is " + deniedElements.size() + " <<<");

            for (Element element : deniedElements) {
                //PermissionDenied 必须注解在方法上，虽然注解有限定(ElementType.METHOD)，但是这边还是做了一层处理,判断是否注解在方法上
                if (element.asType().getKind() == TypeKind.EXECUTABLE) {
                    //获取parent Element
                    TypeElement parentElement = (TypeElement) element.getEnclosingElement();
                    String      qualifiedName = parentElement.getQualifiedName().toString();

                    ProcessInfo info = map.get(qualifiedName);
                    if (info == null) {
                        info = new ProcessInfo();
                        map.put(qualifiedName, info);
                    }

                    if (Utils.isMapNotEmpty(info.getParamsDenyMap())) {
                        throw new RuntimeException("RDPermission::Compiler >>> there is already have method which is annotated by @PermissionDenied in " +
                                qualifiedName);
                    }

                    //扫描方法参数
                    info.resolveDeniedMethod(logger, element);
                }
            }
        }
    }

    /**
     * 生成permissionGranted方法
     */
    public MethodSpec generateGrantMethod(String qualifiedName) {
        ProcessInfo                   info      = map.get(qualifiedName);
        LinkedHashMap<String, Entity> paramsMap = info.getParamsGrantMap();

        /*
          build input parameter like as
          Object proxy
         */
        ParameterSpec specProxy = ParameterSpec.builder(TypeName.OBJECT, "proxy").build();

        // build method : 'permissionGranted'
        MethodSpec.Builder grantMethodBuilder = buildMethod(Consts.METHOD_PERMISSIONGRANTED, paramsMap)
                .addParameter(specProxy)
                .addAnnotation(Override.class);

        StringBuilder argsStrings = new StringBuilder();
        String        packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
        String        className   = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1, qualifiedName.length());

        if (paramsMap.isEmpty() && Utils.isStringNotEmpty(info.getMethodGrantName())) {
            return grantMethodBuilder.addStatement("(($T)proxy)." + info.getMethodGrantName() + "()", ClassName.get(packageName, className)).build();
        } else if (paramsMap.isEmpty() && Utils.isStringEmpty(info.getMethodGrantName())) {
            return grantMethodBuilder.build();
        } else {
            for (String key : paramsMap.keySet()) {
                Entity entity = paramsMap.get(key);
                if (entity.isPMLibAnnotation()) {
                    logger.info(" >>> " + qualifiedName + " / " + "permissionGranted"
                            + "\n " + entity.getParameterName()
                            + "\n " + entity.getParameterType()
                            + " <<<");

                    argsStrings.append(entity.getParameterName()).append(",");
                } else {
                    argsStrings.append(entity.getDefaultValus()).append(",");
                }
            }

            return grantMethodBuilder.addStatement("(($T)proxy)."
                    + info.getMethodGrantName()
                    + "(" + argsStrings.toString().substring(0, argsStrings.length() - 1) + ")", ClassName.get(packageName, className))
                    .build();
        }
    }

    /**
     * 生成permissionDenied方法
     */
    public MethodSpec generateDenyMethod(String qualifiedName) {
        ProcessInfo                   info      = map.get(qualifiedName);
        LinkedHashMap<String, Entity> paramsMap = info.getParamsDenyMap();

        /*
          build input parameter like as
          Object proxy
         */
        ParameterSpec specProxy = ParameterSpec.builder(TypeName.OBJECT, "proxy").build();

        // build method : 'permissionDenied'
        MethodSpec.Builder denyMethodBuilder = buildMethod(Consts.METHOD_PERMISSIONDENIED, paramsMap)
                .addParameter(specProxy)
                .addAnnotation(Override.class);

        StringBuilder argsStrings = new StringBuilder();
        String        packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
        String        className   = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1, qualifiedName.length());

        if (paramsMap.isEmpty() && Utils.isStringNotEmpty(info.getMethodDenyName())) {
            return denyMethodBuilder.addStatement("(($T)proxy)." + info.getMethodDenyName() + "()", ClassName.get(packageName, className)).build();
        } else if (paramsMap.isEmpty() && Utils.isStringEmpty(info.getMethodDenyName())) {
            return denyMethodBuilder.build();
        } else {
            for (String key : paramsMap.keySet()) {
                Entity entity = paramsMap.get(key);
                if (entity.isPMLibAnnotation()) {
                    logger.info(" >>> " + qualifiedName + " / " + "permissionDenied"
                            + "\n " + entity.getParameterName()
                            + "\n " + entity.getParameterType()
                            + " <<<");

                    argsStrings.append(entity.getParameterName()).append(",");
                } else {
                    argsStrings.append(entity.getDefaultValus()).append(",");
                }
            }

            return denyMethodBuilder.addStatement("(($T)proxy)."
                    + info.getMethodDenyName()
                    + "(" + argsStrings.toString().substring(0, argsStrings.length() - 1) + ")", ClassName.get(packageName, className))
                    .build();
        }
    }

    /**
     * 构建方法
     * e.g
     * public void permissionGranted(Object proxy,int requestCode, String[] permissions, int[] grantResults)
     */
    private MethodSpec.Builder buildMethod(String methodName, LinkedHashMap<String, Entity> map) {
        // build method
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC);

        //requestCode
        if (map.containsKey(Consts.ANNOTATION_MAPPING_REQUESTCODE)) {
            methodBuilder.addParameter(TypeName.INT, map.get(Consts.ANNOTATION_MAPPING_REQUESTCODE).getParameterName());
        } else {
            methodBuilder.addParameter(TypeName.INT, Consts.ANNOTATION_MAPPING_REQUESTCODE);
        }

        //permissions
        TypeMirror arrayStringType     = elementUtils.getTypeElement(Consts.STRING).asType();
        TypeName   arrayStringTypeName = TypeName.get(typeUtils.getArrayType(arrayStringType));
        if (map.containsKey(Consts.ANNOTATION_MAPPING_PERMISSIONS)) {
            methodBuilder.addParameter(arrayStringTypeName, map.get(Consts.ANNOTATION_MAPPING_PERMISSIONS).getParameterName());
        } else {
            methodBuilder.addParameter(arrayStringTypeName, Consts.ANNOTATION_MAPPING_PERMISSIONS);
        }

        //grantResults
        TypeName arrayIntTypeName = ArrayTypeName.of(TypeName.INT);
        if (map.containsKey(Consts.ANNOTATION_MAPPING_GRANTRESULTS)) {
            methodBuilder.addParameter(arrayIntTypeName, map.get(Consts.ANNOTATION_MAPPING_GRANTRESULTS).getParameterName());
        } else {
            methodBuilder.addParameter(arrayIntTypeName, Consts.ANNOTATION_MAPPING_GRANTRESULTS);
        }

        return methodBuilder;
    }
}
