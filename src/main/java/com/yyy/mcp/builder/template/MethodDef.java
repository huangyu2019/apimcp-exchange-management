package com.yyy.mcp.builder.template;

public class MethodDef {
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * @tool注解的描述信息
     */
    private String toolAnnotationDescField;

    /**
     * tool方法对应的API编码
     */
    private String apiCode;

    /**
     * 方法返回类型信息
     */
    private Class<?> returnType;

    public MethodDef() {

    }

    public MethodDef(String methodName, String toolAnnotDesc, String apiCode, Class<?> returnType) {
        this.methodName = methodName;
        this.toolAnnotationDescField = toolAnnotDesc;
        this.apiCode = apiCode;
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getToolAnnotationDescField() {
        return toolAnnotationDescField;
    }

    public void setToolAnnotationDescField(String toolAnnotationDescField) {
        this.toolAnnotationDescField = toolAnnotationDescField;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
