package com.consultorio.orquestadoria.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaudeResponse {

    private List<ContenidoBloque> content;

    @JsonProperty("stop_reason")
    private String stopReason;

    public List<ContenidoBloque> getContent() { return content; }
    public void setContent(List<ContenidoBloque> content) { this.content = content; }
    public String getStopReason() { return stopReason; }
    public void setStopReason(String stopReason) { this.stopReason = stopReason; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContenidoBloque {
        private String type;
        private String text;
        private String id;
        private String name;
        private Map<String, Object> input;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Map<String, Object> getInput() { return input; }
        public void setInput(Map<String, Object> input) { this.input = input; }
    }
}