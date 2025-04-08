package com.luxx.seed.config.auth;

import cn.dev33.satoken.config.SaTokenConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TokenConfig {

    @Bean
    @Primary
    public SaTokenConfig getTokenConfig() {
        return new SaTokenConfig()
                .setIsPrint(false)
                .setTokenName("Authorization")
                .setTimeout(60 * 60)
                .setTokenStyle("uuid")
                .setIsLog(false)
                .setIsReadCookie(true);  //是否从Cookie读取Token
    }
}
