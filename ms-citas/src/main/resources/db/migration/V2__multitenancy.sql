ALTER TABLE citas ADD COLUMN organizacion_id BIGINT;
ALTER TABLE lista_espera ADD COLUMN organizacion_id BIGINT;

-- Como no hay forma de derivar la organizacion de citas existentes sin consultar
-- a ms-medicos, las marcamos con la organizacion default (id=1) para desarrollo.
-- En un entorno real con datos de produccion, esto requeriria un script de backfill
-- que consulte ms-medicos por cada medicoId.
UPDATE citas SET organizacion_id = 1 WHERE organizacion_id IS NULL;
UPDATE lista_espera SET organizacion_id = 1 WHERE organizacion_id IS NULL;

ALTER TABLE citas ALTER COLUMN organizacion_id SET NOT NULL;
ALTER TABLE lista_espera ALTER COLUMN organizacion_id SET NOT NULL;

CREATE INDEX idx_citas_organizacion ON citas(organizacion_id);
CREATE INDEX idx_lista_espera_organizacion ON lista_espera(organizacion_id);