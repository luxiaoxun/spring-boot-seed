package com.luxx.flow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("luxiaoxun001@qq.com");
        contact.setName("luxiaoxun");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Flow API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to engine tutorials.")
                .license(mitLicense);

        return new OpenAPI().info(info);
    }
}
