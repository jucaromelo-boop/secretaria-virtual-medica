package com.consultorio.citas.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(ServiceAuthInterceptor serviceAuthInterceptor, CorrelationIdInterceptor correlationIdInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(serviceAuthInterceptor);
        restTemplate.getInterceptors().add(correlationIdInterceptor);
        return restTemplate;
    }
}