CREATE TABLE paciente_organizaciones (
                                         paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
                                         organizacion_id BIGINT NOT NULL,
                                         PRIMARY KEY (paciente_id, organizacion_id)
);

-- Backfill: asociamos todos los pacientes existentes a la organizacion default (id=1),
-- igual que hicimos en ms-citas y ms-medicos.
INSERT INTO paciente_organizaciones (paciente_id, organizacion_id)
SELECT id, 1 FROM pacientes;

CREATE INDEX idx_paciente_organizaciones_org ON paciente_organizaciones(organizacion_id);