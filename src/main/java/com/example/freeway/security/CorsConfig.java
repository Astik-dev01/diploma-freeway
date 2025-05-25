package com.example.freeway.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class CorsConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("https://freeway-edu.netlify.app")
//                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .exposedHeaders("Authorization")
                    .allowCredentials(true);
        }
    }

