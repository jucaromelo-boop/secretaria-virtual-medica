package com.consultorio.orquestadoria.client;

import com.consultorio.orquestadoria.client.dto.ClaudeMessage;
import com.consultorio.orquestadoria.client.dto.ClaudeRequest;
import com.consultorio.orquestadoria.client.dto.ClaudeResponse;
import com.consultorio.orquestadoria.client.dto.ToolDefinition;
import com.consultorio.orquestadoria.skill.SkillExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ClaudeClient {

    private static final Logger log = LoggerFactory.getLogger(ClaudeClient.class);
    private static final int MAX_ITERACIONES = 8;

    private final RestTemplate restTemplate;
    private final SkillExecutor skillExecutor;

    @Value("${anthropic.api-key}")
    private String apiKey;

    @Value("${anthropic.model}")
    private String model;

    @Value("${anthropic.api-url}")
    private String apiUrl;

    public ClaudeClient(@Qualifier("restTemplateExterno") RestTemplate restTemplate, SkillExecutor skillExecutor) {
        this.restTemplate = restTemplate;
        this.skillExecutor = skillExecutor;
    }

    public String enviarMensaje(String systemPrompt, List<ClaudeMessage> historial, List<ToolDefinition> tools,
                                String telefonoConversacion, Long organizacionId, boolean esMedico) {
        List<ClaudeMessage> conversacion = new ArrayList<>(historial);

        for (int i = 0; i < MAX_ITERACIONES; i++) {
            ClaudeResponse response;
            try {
                response = llamarClaude(systemPrompt, conversacion, tools);
            } catch (Exception ex) {
                log.error("Error llamando a la API de Claude", ex);
                return "Disculpa, tuve un problema para procesar tu mensaje. ¿Puedes intentar de nuevo?";
            }

            if (response == null || response.getContent() == null) {
                log.warn("Respuesta de Claude nula o sin contenido");
                return "Disculpa, tuve un problema para procesar tu mensaje. ¿Puedes intentar de nuevo?";
            }

            log.info("stopReason: {}, cantidad de bloques: {}", response.getStopReason(), response.getContent().size());
            for (ClaudeResponse.ContenidoBloque bloque : response.getContent()) {
                log.info("Bloque tipo={}, text={}, name={}", bloque.getType(), bloque.getText(), bloque.getName());
            }

            if (!"tool_use".equals(response.getStopReason())) {
                return extraerTexto(response);
            }

            // Claude quiere usar una o mas herramientas
            conversacion.add(new ClaudeMessage("assistant", construirBloquesAssistant(response)));

            List<Map<String, Object>> resultadosTools = new ArrayList<>();
            for (ClaudeResponse.ContenidoBloque bloque : response.getContent()) {
                if ("tool_use".equals(bloque.getType())) {
                    log.info("Ejecutando skill: {} con input: {}", bloque.getName(), bloque.getInput());
                    String resultado = skillExecutor.ejecutar(bloque.getName(), bloque.getInput(), telefonoConversacion, organizacionId, esMedico);
                    log.info("Resultado de la skill {}: {}", bloque.getName(), resultado);
                    resultadosTools.add(Map.of(
                            "type", "tool_result",
                            "tool_use_id", bloque.getId(),
                            "content", resultado
                    ));
                }
            }

            conversacion.add(new ClaudeMessage("user", resultadosTools));
        }

        return "Disculpa, esta solicitud requiere demasiados pasos. ¿Puedes reformular tu pregunta?";
    }

    private ClaudeResponse llamarClaude(String systemPrompt, List<ClaudeMessage> conversacion, List<ToolDefinition> tools) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ClaudeRequest request = new ClaudeRequest(model, 1024, systemPrompt, conversacion, tools);
        HttpEntity<ClaudeRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(apiUrl, entity, ClaudeResponse.class);
    }

    private List<Map<String, Object>> construirBloquesAssistant(ClaudeResponse response) {
        List<Map<String, Object>> bloques = new ArrayList<>();
        for (ClaudeResponse.ContenidoBloque bloque : response.getContent()) {
            if ("text".equals(bloque.getType())) {
                bloques.add(Map.of("type", "text", "text", bloque.getText()));
            } else if ("tool_use".equals(bloque.getType())) {
                bloques.add(Map.of(
                        "type", "tool_use",
                        "id", bloque.getId(),
                        "name", bloque.getName(),
                        "input", bloque.getInput()
                ));
            }
        }
        return bloques;
    }

    private String extraerTexto(ClaudeResponse response) {
        return response.getContent().stream()
                .filter(b -> "text".equals(b.getType()))
                .map(ClaudeResponse.ContenidoBloque::getText)
                .findFirst()
                .orElse("Disculpa, no pude generar una respuesta. ¿Puedes intentar de nuevo?");
    }
}