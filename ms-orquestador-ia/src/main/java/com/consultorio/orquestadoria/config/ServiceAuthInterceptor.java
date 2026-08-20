package com.consultorio.orquestadoria.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceAuthInterceptor implements ClientHttpRequestInterceptor {

    private final ServiceTokenProvider serviceTokenProvider;

    public ServiceAuthInterceptor(ServiceTokenProvider serviceTokenProvider) {
        this.serviceTokenProvider = serviceTokenProvider;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().setBearerAuth(serviceTokenProvider.obtenerToken());
        return execution.execute(request, body);
    }
}