package com.luxx.seed.config.swagger;

import com.luxx.seed.controller.BaseController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        RequestParameterBuilder tokenBuilder = new RequestParameterBuilder();
        tokenBuilder.name(BaseController.AUTH_TOKEN)
                .description("access_token")
                .required(false)
                .in("header")
                .accepts(Collections.singleton(MediaType.APPLICATION_JSON))
                .build();

        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(tokenBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.luxx.seed.controller"))
                .paths(PathSelectors.any())
                .build().globalRequestParameters(parameters);
    }

    // 构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("XX平台")
                .contact(new Contact("luxiaoxun", "", ""))
                .version("1.0")
                .build();
    }
}
