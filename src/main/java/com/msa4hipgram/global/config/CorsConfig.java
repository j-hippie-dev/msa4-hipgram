package com.msa4hipgram.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
public record CorsConfig(
        List<String> allowedOrigins
        , Long maxAge
) {
}
