package com.xianmao.bean;

import java.util.List;

/**
 * @ClassName MethodInfo
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-16 11:14
 * @Version 1.0
 */
public class MethodInfo {
    /**
     * 所在类全类名
     */
    private String classAllName;
    /**
     * 所在类简单类名
     */
    private String classSimpleName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数列表
     */
    private List<String> paramNames;
    /**
     * 方法行号
     */
    private Integer lineNumber;

    /**
     * 构造
     *
     * @param classAllName    所在类全类名
     * @param classSimpleName 所在类简单类名
     * @param methodName      方法名称
     * @param paramNames      参数列表
     * @param lineNumber      方法行号
     */
    public MethodInfo(String classAllName, String classSimpleName, String methodName, List<String> paramNames, Integer lineNumber) {
        this.classAllName = classAllName;
        this.classSimpleName = classSimpleName;
        this.methodName = methodName;
        this.paramNames = paramNames;
        this.lineNumber = lineNumber;
    }

    public String getClassAllName() {
        return classAllName;
    }

    public void setClassAllName(String classAllName) {
        this.classAllName = classAllName;
    }

    public String getClassSimpleName() {
        return classSimpleName;
    }

    public void setClassSimpleName(String classSimpleName) {
        this.classSimpleName = classSimpleName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void setParamNames(List<String> paramNames) {
        this.paramNames = paramNames;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
}
