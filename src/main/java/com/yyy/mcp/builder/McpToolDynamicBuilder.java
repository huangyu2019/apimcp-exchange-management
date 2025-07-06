package com.yyy.mcp.builder;

import com.alibaba.fastjson.JSONObject;
import com.yyy.mcp.builder.delegate.ToolMethodDelegate;
import com.yyy.mcp.builder.template.MethodDef;
import com.yyy.mcp.builder.template.ParameterDef;
import com.yyy.mcp.tool.BaseTool;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Annotatable;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * <p>mcp tool构建器，把普通API转成MCP</p>
 *
 * @since 2025-07-05
 */
public class McpToolDynamicBuilder<T extends BaseTool> {
    protected static final String TOOL_PACKAGE_PATH = "com.yyy.mcp.tool."; // System.getProperty("toolClassPath");

    protected Builder<BaseTool> toolBuilder;

    protected Builder.MethodDefinition<? extends BaseTool> methodDefinition;

    public McpToolDynamicBuilder(String toolClassName) {
        this.toolBuilder = new ByteBuddy().subclass(BaseTool.class).name(toolClassName);
    }

    /**
     * 动态创建方法，可连续创建不同的方法
     * 
     * @param methodDef 待创建的方法定义信息
     * @param parameterDefs 方法参数定义信息
     * @return this
     */
    public McpToolDynamicBuilder<T> createMethod(MethodDef methodDef, ParameterDef... parameterDefs) {
        // 定义方法
        Initial<? extends BaseTool> baseToolInitial = null;
        if (this.methodDefinition != null) {
            baseToolInitial = this.methodDefinition
                    .defineMethod(methodDef.getMethodName(), methodDef.getReturnType(), Modifier.PUBLIC);
        } else {
            baseToolInitial = this.toolBuilder
                    .defineMethod(methodDef.getMethodName(), methodDef.getReturnType(), Modifier.PUBLIC);
        }

        // 定义方法参数
        Annotatable<? extends BaseTool> annotatable = AnnotationBuilder.toolParamMethodParamAnnotation(baseToolInitial, parameterDefs);
        // 定义方法注解与异常等，并设置方法体内容为调用另一个通用类方法
        this.methodDefinition = annotatable
                .throwing(Throwable.class)
                .intercept(MethodDelegation.to(ToolMethodDelegate.class))
                .annotateMethod(AnnotationBuilder.toolMethodAnnotation(methodDef.getToolAnnotationDescField()));
        return this;
    }

    /**
     *
     * 创建动态类实例
     *
     * @param toolClassName 类文件名称
     * @return BaseTool子类实例
     * @throws NoSuchMethodException 如果没有相应的构造器
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException if the class that declares the underlying constructor represents an abstract class.
     * @throws IllegalAccessException  if this Constructor object is enforcing Java language access control and the underlying constructor is inaccessible.
     */
    public BaseTool createInstance(String toolClassName) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        DynamicType.Unloaded<? extends BaseTool> unloaded = this.methodDefinition.make();
        outputClazzFile(unloaded.getBytes(), BaseTool.class.getName() + "$" + toolClassName);
        return unloaded
                .load(this.getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    public static void outputClazzFile(byte[] classBytes, String clazzName) {
        String classPath = BaseTool.class.getResource("/").getPath() + clazzName + ".class";
        try (FileOutputStream classOutputStream = new FileOutputStream(new File(classPath))) {
            classOutputStream.write(classBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class AnnotationBuilder {
        /**
         * 给方法设置注解Tool
         * 
         * @return 方法注解描述
         */
        public static AnnotationDescription toolMethodAnnotation(String description) {
            return AnnotationDescription.Builder.ofType(Tool.class).define("description", description).build();
        }

        /**
         * 给方法参数设置toolParam注解
         * 
         * @param initial 类的初始化构建器
         * @param parameterDefs 方法参数定义
         * @return 方法参数注解信息
         */
        public static Annotatable<? extends BaseTool> toolParamMethodParamAnnotation(Initial<? extends BaseTool> initial, ParameterDef... parameterDefs) {
            Annotatable<? extends BaseTool> paramAnnatatable = null;
            for (int i = 0; i < parameterDefs.length; i++) {
                if (i == 0) {
                    paramAnnatatable = initial
                            .withParameter(parameterDefs[i].getParamType(), parameterDefs[i].getParamName())
                            .annotateParameter(toolParamAnnotDescription(parameterDefs[i]));
                } else {
                    paramAnnatatable = paramAnnatatable
                            .withParameter(parameterDefs[i].getParamType(), parameterDefs[i].getParamName())
                            .annotateParameter(toolParamAnnotDescription(parameterDefs[i]));
                }
            }
            
            return paramAnnatatable;
        }

        private static AnnotationDescription toolParamAnnotDescription (ParameterDef parameterDef) {
            return AnnotationDescription.Builder.ofType(ToolParam.class)
                    .define("description",
                            StringUtils.isNotBlank(parameterDef.getParamDescription()) ? parameterDef.getParamDescription() : parameterDef.getParamName())
                    .define("required", true)
                    .build();
        }
    }
}
