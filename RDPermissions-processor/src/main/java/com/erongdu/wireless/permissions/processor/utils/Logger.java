package com.erongdu.wireless.permissions.processor.utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/1/12 下午2:33
 * <p>
 * Description: 日志输出类
 */
public class Logger {
    private Messager messager;

    public Logger(Messager messager) {
        this.messager = messager;
    }

    public void info(String info) {
        if (Utils.isStringNotEmpty(info)) {
            messager.printMessage(Diagnostic.Kind.NOTE, Consts.PROJECT + info);
        }
    }

    public void error(CharSequence error) {
        if (Utils.isStringNotEmpty(error)) {
            messager.printMessage(Diagnostic.Kind.ERROR, Consts.PREFIX_OF_LOGGER + error);
        }
    }

    public void error(Throwable error) {
        if (error != null) {
            messager.printMessage(Diagnostic.Kind.ERROR, Consts.PREFIX_OF_LOGGER + error.getMessage());
        }
    }

    public void error(Element element, CharSequence error) {
        if (Utils.isStringNotEmpty(error)) {
            messager.printMessage(Diagnostic.Kind.ERROR, Consts.PREFIX_OF_LOGGER + error, element);
        }
    }

    public void warning(CharSequence warning) {
        if (Utils.isStringNotEmpty(warning)) {
            messager.printMessage(Diagnostic.Kind.WARNING, Consts.PREFIX_OF_LOGGER + warning);
        }
    }
}
