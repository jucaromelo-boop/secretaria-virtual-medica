CREATE TABLE auditoria (
                           id BIGSERIAL PRIMARY KEY,
                           usuario VARCHAR(255) NOT NULL,
                           accion VARCHAR(100) NOT NULL,
                           entidad_tipo VARCHAR(100) NOT NULL,
                           entidad_id BIGINT NOT NULL,
                           organizacion_id BIGINT,
                           correlation_id VARCHAR(100),
                           detalles VARCHAR(1000),
                           fecha TIMESTAMP NOT NULL
);

CREATE INDEX idx_auditoria_entidad ON auditoria(entidad_tipo, entidad_id);
CREATE INDEX idx_auditoria_usuario ON auditoria(usuario);
CREATE INDEX idx_auditoria_organizacion ON auditoria(organizacion_id);
CREATE INDEX idx_auditoria_fecha ON auditoria(fecha);