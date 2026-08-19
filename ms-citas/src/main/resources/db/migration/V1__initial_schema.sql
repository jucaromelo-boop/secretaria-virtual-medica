CREATE TABLE citas (
                       id BIGSERIAL PRIMARY KEY,
                       paciente_id BIGINT NOT NULL,
                       medico_id BIGINT NOT NULL,
                       fecha_hora TIMESTAMP NOT NULL,
                       duracion_minutos INTEGER NOT NULL,
                       estado VARCHAR(20) NOT NULL,
                       tipo_consulta VARCHAR(20) NOT NULL DEFAULT 'PRIMERA_VEZ'
);

CREATE TABLE lista_espera (
                              id BIGSERIAL PRIMARY KEY,
                              paciente_id BIGINT NOT NULL,
                              medico_id BIGINT NOT NULL,
                              hora_inicio_preferida TIME NOT NULL,
                              hora_fin_preferida TIME NOT NULL,
                              fecha_limite DATE NOT NULL,
                              estado VARCHAR(20) NOT NULL,
                              fecha_registro TIMESTAMP NOT NULL
);

CREATE TABLE lista_espera_dias (
                                   lista_espera_id BIGINT NOT NULL REFERENCES lista_espera(id),
                                   dia_semana VARCHAR(20) NOT NULL
);

CREATE INDEX idx_citas_medico_fecha ON citas(medico_id, fecha_hora);
CREATE INDEX idx_citas_paciente ON citas(paciente_id);
CREATE INDEX idx_lista_espera_medico ON lista_espera(medico_id, estado);