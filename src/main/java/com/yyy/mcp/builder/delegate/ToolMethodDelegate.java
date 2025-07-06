package com.yyy.mcp.builder.delegate;

import com.alibaba.fastjson.JSONObject;
import com.yyy.mcp.builder.template.MethodDef;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ToolMethodDelegate {
    /**
     * 动态tool工具方法的代理方法
     * 动态方法的方法名作为唯一映射，可获取配置中方法的所有参数名称等，用于代理转发逻辑
     *
     * @param args 被代理方法的全部参数
     * @param method 被代理的方法对象
     * @return 方法返回值
     */
    @RuntimeType
    public static JSONObject apiProxy (@AllArguments Object[] args, @Origin Method method) {
        System.out.println("代理方法的参数个数： " + args.length + "，接口编码是：" + method.getName());

        JSONObject result = new JSONObject();
        result.put("code", "000000");
        result.put("msg", "result from source api");

        for (Object arg : args) {
            if (arg instanceof String) {

            } else if (arg instanceof Integer) {

            } else if (arg instanceof Boolean) {

            } else if (arg instanceof Long) {

            } else if (arg instanceof List) {

            } else if (arg instanceof Map<?,?>) {

            } else {
                throw new RuntimeException("暂不支持String,boolean,integer,long,list,map之外的数据类型");
            }
        }
        return result;
    }
}
