package com.it.graphql_gateway_skeleton.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Enhanced HTTP client with GET/POST support and error handling
 * 
 * @Author Andy wang
 * @Created 2025/9/12
 */
public class RestClient {
    private final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(3))
        .build();

    /**
     * Performs HTTP GET request
     */
    public String get(String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
            return executeRequest(req, url);
        } catch (Exception e) {
            throw new RuntimeException("GET request failed: " + url, e);
        }
    }

    /**
     * Performs HTTP POST request with JSON body
     */
    public String post(String url, String jsonBody) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
            return executeRequest(req, url);
        } catch (Exception e) {
            throw new RuntimeException("POST request failed: " + url, e);
        }
    }

    /**
     * Common request execution logic
     */
    private String executeRequest(HttpRequest request, String url) throws Exception {
        HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
            return resp.body();
        } else if (resp.statusCode() == 404) {
            throw new RuntimeException("Resource not found (404): " + url);
        } else if (resp.statusCode() >= 500) {
            throw new RuntimeException("Server error (" + resp.statusCode() + "): " + url);
        } else {
            throw new RuntimeException("HTTP " + resp.statusCode() + " for " + url + 
                                     ". Response: " + resp.body());
        }
    }
}
