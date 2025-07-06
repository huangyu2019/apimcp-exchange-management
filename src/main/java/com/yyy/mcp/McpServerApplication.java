package com.yyy.mcp;

import com.yyy.mcp.service.CommonToolService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider commonTools(CommonToolService commonToolService) {
        return MethodToolCallbackProvider.builder().toolObjects(commonToolService).build();
    }

}
