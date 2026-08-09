package com.consultorio.orquestadoria.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClaudeRequest {

    private String model;

    @JsonProperty("max_tokens")
    private int maxTokens;

    private String system;
    private List<ClaudeMessage> messages;

    public ClaudeRequest(String model, int maxTokens, String system, List<ClaudeMessage> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.system = system;
        this.messages = messages;
    }

    public String getModel() { return model; }
    public int getMaxTokens() { return maxTokens; }
    public String getSystem() { return system; }
    public List<ClaudeMessage> getMessages() { return messages; }
}