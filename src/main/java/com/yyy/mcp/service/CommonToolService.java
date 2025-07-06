package com.yyy.mcp.service;

import com.alibaba.fastjson.JSONObject;
import com.yyy.mcp.builder.McpToolDynamicBuilder;
import com.yyy.mcp.builder.template.MethodDef;
import com.yyy.mcp.builder.template.ParameterDef;
import com.yyy.mcp.tool.BaseTool;
import org.springframework.stereotype.Service;

@Service
public class CommonToolService {

    public static void main(String[] args) throws Exception {
//        DynamicType.Unloaded<BaseTool> unloaded = new ByteBuddy()
//                .subclass(BaseTool.class)
//                .name("TestToolService")
//                .defineMethod("getUserInfo", JSONObject.class, Modifier.PUBLIC)
//                .withParameter(String.class, "username")
//                .annotateParameter(AnnotationDescription.Builder.ofType(ToolParam.class).define("description", "姓名").build())
//                .withParameter(String.class, "sex")
//                .annotateParameter(AnnotationDescription.Builder.ofType(ToolParam.class).define("description", "性别").build())
//                .throwing(Throwable.class)
//                .intercept(MethodDelegation.to(ToolMethodDelegate.class))
//                .annotateMethod(AnnotationDescription.Builder.ofType(Tool.class).build()).make();
//        McpToolDynamicBuilder.outputClazzFile(unloaded.getBytes(), BaseTool.class.getName() + "$" + "TestToolService");
//        BaseTool baseTool = unloaded
//                .load(CommonToolService.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
//                .getLoaded()
//                .getDeclaredConstructor()
//                .newInstance();
//
//        System.out.println(baseTool.getClass().getDeclaredMethod("getUserInfo", String.class, String.class).invoke(baseTool, "xxx", "男"));

        try {
            MethodDef methodDef = new MethodDef("getUserInfo", "获取用户信息", "userInfo", JSONObject.class);
            ParameterDef parameterDef1 = new ParameterDef(String.class, "username", "用户名字");
            ParameterDef parameterDef2 = new ParameterDef(String.class, "sex", "性别");
            ParameterDef parameterDef3 = new ParameterDef(String.class, "apiCode", "getUserInfo");

            MethodDef methodDef2 = new MethodDef("getUserInfo2", "获取用户信息", "userInfo2", JSONObject.class);
            McpToolDynamicBuilder<BaseTool> baseToolMcpToolDynamicBuilder = new McpToolDynamicBuilder<>("UserInfoToolOne");
            BaseTool instance = baseToolMcpToolDynamicBuilder
                    .createMethod(methodDef, parameterDef1, parameterDef2)
                    .createMethod(methodDef2, parameterDef1, parameterDef2)
                    .createInstance("UserInfoToolOne");
            System.out.println(instance.getClass().getDeclaredMethod("getUserInfo", String.class, String.class).invoke(instance, "xxx", "男"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
