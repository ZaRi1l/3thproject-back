package com._thproject._thproject_web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("3차 프로젝트 API 문서") // API 문서의 제목
                .version("1.0.0") // API 버전
                .description("3차 프로젝트의 백엔드 API 명세서입니다."); // API에 대한 상세한 설명
        
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
