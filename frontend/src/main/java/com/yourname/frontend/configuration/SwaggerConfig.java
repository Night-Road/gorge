package com.yourname.frontend.configuration;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/7/2022 10:22 AM
 */

@Configuration
//启用swagger2
@EnableSwagger2
//启用swagger增强UI
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //这里写的是API接口所在的包位置
                .apis(RequestHandlerSelectors.basePackage("com.yourname.frontend.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}

