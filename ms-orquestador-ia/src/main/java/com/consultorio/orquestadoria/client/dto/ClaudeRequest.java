package com.consultorio.orquestadoria.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClaudeRequest {

    private String model;

    @JsonProperty("max_tokens")
    private int maxTokens;

    private String system;
    private List<ClaudeMessage> messages;
    private List<ToolDefinition> tools;

    public ClaudeRequest(String model, int maxTokens, String system, List<ClaudeMessage> messages, List<ToolDefinition> tools) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.system = system;
        this.messages = messages;
        this.tools = tools;
    }

    public String getModel() { return model; }
    public int getMaxTokens() { return maxTokens; }
    public String getSystem() { return system; }
    public List<ClaudeMessage> getMessages() { return messages; }
    public List<ToolDefinition> getTools() { return tools; }
}