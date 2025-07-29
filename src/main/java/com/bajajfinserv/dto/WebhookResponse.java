package com.bajajfinserv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookResponse {
    @JsonProperty("webhook")
    private String webhook;
    
    // FINAL FIX: This now correctly matches the key from the server's response.
    @JsonProperty("accessToken")
    private String accessToken;

    // Default constructor is good practice for Jackson
    public WebhookResponse() {
    }

    // Getters and setters
    public String getWebhook() { return webhook; }
    public void setWebhook(String webhook) { this.webhook = webhook; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    @Override
    public String toString() {
        // This will now show [RECEIVED] for a non-null token
        return "WebhookResponse{" +
               "webhook='" + webhook + '\'' +
               ", accessToken='" + (accessToken != null && !accessToken.isEmpty() ? "[RECEIVED]" : "null") + '\'' +
               '}';
    }
}
