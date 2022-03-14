package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)  // 2.0
                .select()   // 스웨거에 의해 노출되는 end point 를 제어하는 하나의 방법
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/szs/**"))    // /szs/** 인 URL들만 필터링
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Backend Engineer Project")
                .description("2022-02-18 ~ 2022-02-25")
                .version("1.0")
                .build();
    }
}
