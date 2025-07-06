package com.yyy.mcp.builder.template;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ParameterDef {
    private String paramName;

    private Class<?> paramType;

    private String paramDescription;

    public ParameterDef() {

    }

    public ParameterDef(Class<?> paramType, String paramName, String paramDescription) {
        this.paramType = paramType;
        this.paramName = paramName;
        this.paramDescription = paramDescription;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Class<?> getParamType() {
        return paramType;
    }

    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
    }
}
