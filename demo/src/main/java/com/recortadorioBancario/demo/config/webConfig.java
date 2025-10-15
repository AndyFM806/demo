package com.recortadorioBancario.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // ✅ DOMINIOS PERMITIDOS
                        .allowedOriginPatterns(
                                "https://frontrecordatorio.onrender.com", // tu frontend en Render
                                "http://localhost:5500",                 // desarrollo local
                                "http://127.0.0.1:5500"
                        )
                        // ✅ MÉTODOS PERMITIDOS
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // ✅ PERMITE CABECERAS
                        .allowedHeaders("*")
                        // ✅ PERMITE CREDENCIALES (cookies, tokens)
                        .allowCredentials(true);
            }
        };
    }
}
