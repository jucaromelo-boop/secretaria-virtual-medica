CREATE TABLE notificaciones (
                                id BIGSERIAL PRIMARY KEY,
                                cita_id BIGINT NOT NULL,
                                paciente_id BIGINT NOT NULL,
                                medico_id BIGINT NOT NULL,
                                tipo VARCHAR(30) NOT NULL,
                                mensaje VARCHAR(1000) NOT NULL,
                                estado VARCHAR(20) NOT NULL,
                                fecha_creacion TIMESTAMP NOT NULL,
                                fecha_envio TIMESTAMP
);

CREATE INDEX idx_notificaciones_medico ON notificaciones(medico_id);
CREATE INDEX idx_notificaciones_cita ON notificaciones(cita_id);