package com.example.chekdocumentservice.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class CrptApi {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    private final BlockingQueue<Instant> requestQueue;
    private final long timeWindowMillis;

    public CrptApi(RestTemplate restTemplate,
                   @Value("${chek.api.url}") String apiUrl,
                   @Value("${api.rate.limit.window.seconds}") int timeWindowSeconds,
                   @Value("${api.rate.limit.requests}") int requestLimit) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.timeWindowMillis = TimeUnit.SECONDS.toMillis(timeWindowSeconds);
        this.requestQueue = new LinkedBlockingQueue<>(requestLimit);
    }

    @PostConstruct
    public void init() {
        // Clean up expired requests periodically
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    cleanUpExpiredRequests();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public ResponseEntity<String> createDocument(Object document, String signature) {
        try {
            if (requestQueue.size() >= requestQueue.remainingCapacity()) {
                requestQueue.take();
            }
            requestQueue.put(Instant.now());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + signature);
            headers.set("Content-Type", "application/json");
            HttpEntity<Object> request = new HttpEntity<>(document, headers);

            return restTemplate.exchange(apiUrl + "/create", HttpMethod.POST, request, String.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void cleanUpExpiredRequests() {
        Instant now = Instant.now();
        requestQueue.removeIf(timestamp -> Duration.between(timestamp, now).toMillis() > timeWindowMillis);
    }
}
