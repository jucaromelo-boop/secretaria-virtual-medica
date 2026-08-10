package com.consultorio.orquestadoria.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean("restTemplateExterno")
    public RestTemplate restTemplateExterno() {
        return new RestTemplate();
    }

    @Bean("restTemplateInterno")
    @LoadBalanced
    public RestTemplate restTemplateInterno() {
        return new RestTemplate();
    }
}