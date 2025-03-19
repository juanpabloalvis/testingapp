package com.jp.testingapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalServiceClient {

    private final RestClient restClient;

    public ExternalServiceClient(RestClient restClientBuilder) {
        this.restClient = restClientBuilder;
    }

    public String callExternalService(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}