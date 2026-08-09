package com.consultorio.orquestadoria.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaudeResponse {

    private List<ContenidoBloque> content;

    public List<ContenidoBloque> getContent() { return content; }
    public void setContent(List<ContenidoBloque> content) { this.content = content; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContenidoBloque {
        private String type;
        private String text;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}