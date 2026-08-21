CREATE TABLE organizaciones (
                                id BIGSERIAL PRIMARY KEY,
                                nombre VARCHAR(255) NOT NULL UNIQUE,
                                codigo_identificador VARCHAR(100) UNIQUE,
                                activo BOOLEAN NOT NULL DEFAULT TRUE,
                                fecha_registro TIMESTAMP NOT NULL
);

INSERT INTO organizaciones (nombre, codigo_identificador, activo, fecha_registro)
VALUES ('Clinica Corazon Sano', 'CLINICA-DEFAULT', TRUE, NOW());

ALTER TABLE medicos ADD COLUMN organizacion_id BIGINT REFERENCES organizaciones(id);

UPDATE medicos SET organizacion_id = (SELECT id FROM organizaciones WHERE codigo_identificador = 'CLINICA-DEFAULT');

ALTER TABLE medicos ALTER COLUMN organizacion_id SET NOT NULL;

CREATE INDEX idx_medicos_organizacion ON medicos(organizacion_id);