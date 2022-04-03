package com.example.project.config;

import com.example.project.app.common.util.JwtManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 이승환
 * @since 2022-04-03
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity-in-seconds}")
    private long tokenValidityInSeconds;

    @Bean
    public JwtManager jwtManager() {
        return new JwtManager(jwtSecret, tokenValidityInSeconds);
    }
}
