package com.consultorio.citas.idempotencia;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotenciaCache {

    private static final long TTL_MINUTOS = 10;

    private final Map<String, EntradaCache> cache = new ConcurrentHashMap<>();

    public Object obtener(String key) {
        EntradaCache entrada = cache.get(key);
        if (entrada == null || entrada.expiro()) {
            cache.remove(key);
            return null;
        }
        return entrada.valor();
    }

    public void guardar(String key, Object valor) {
        cache.put(key, new EntradaCache(valor, Instant.now().plusSeconds(TTL_MINUTOS * 60)));
    }

    private record EntradaCache(Object valor, Instant expiracion) {
        boolean expiro() {
            return Instant.now().isAfter(expiracion);
        }
    }
}