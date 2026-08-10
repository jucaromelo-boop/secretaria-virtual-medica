package com.consultorio.orquestadoria.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ToolDefinition {

    private String name;
    private String description;

    @JsonProperty("input_schema")
    private Map<String, Object> inputSchema;

    public ToolDefinition(String name, String description, Map<String, Object> inputSchema) {
        this.name = name;
        this.description = description;
        this.inputSchema = inputSchema;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, Object> getInputSchema() { return inputSchema; }
}