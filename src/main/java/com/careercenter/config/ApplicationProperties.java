package com.careercenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app")
public class ApplicationProperties {

    private String jwtSecret;
    private String jwtExpiration;
    private String allowedOrigins;
}
