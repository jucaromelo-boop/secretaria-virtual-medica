package com.consultorio.orquestadoria.client.dto;

public class ClaudeMessage {

    private String role;
    private Object content; // puede ser String (texto simple) o List<Map> (bloques estructurados)

    public ClaudeMessage() {
    }

    public ClaudeMessage(String role, Object content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Object getContent() { return content; }
    public void setContent(Object content) { this.content = content; }
}