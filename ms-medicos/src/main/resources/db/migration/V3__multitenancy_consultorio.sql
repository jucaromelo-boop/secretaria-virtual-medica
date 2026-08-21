ALTER TABLE consultorios ADD COLUMN organizacion_id BIGINT REFERENCES organizaciones(id);

UPDATE consultorios c
SET organizacion_id = (SELECT m.organizacion_id FROM medicos m WHERE m.id = c.medico_id);

ALTER TABLE consultorios ALTER COLUMN organizacion_id SET NOT NULL;

CREATE INDEX idx_consultorios_organizacion ON consultorios(organizacion_id);