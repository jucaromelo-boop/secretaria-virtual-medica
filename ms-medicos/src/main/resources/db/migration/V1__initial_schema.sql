CREATE TABLE especialidades (
                                id BIGSERIAL PRIMARY KEY,
                                nombre VARCHAR(255) NOT NULL UNIQUE,
                                descripcion VARCHAR(1000),
                                activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE seguros (
                         id BIGSERIAL PRIMARY KEY,
                         nombre VARCHAR(255) NOT NULL UNIQUE,
                         activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE medicos (
                         id BIGSERIAL PRIMARY KEY,
                         nombre_completo VARCHAR(255) NOT NULL,
                         cedula_profesional VARCHAR(255) NOT NULL UNIQUE,
                         universidad VARCHAR(255),
                         anio_graduacion INTEGER,
                         verificado BOOLEAN NOT NULL DEFAULT FALSE,
                         especialidad_principal_id BIGINT NOT NULL REFERENCES especialidades(id),
                         biografia VARCHAR(2000),
                         foto_url VARCHAR(500),
                         telefono_personal VARCHAR(50),
                         email VARCHAR(255),
                         fecha_registro TIMESTAMP NOT NULL,
                         fecha_ultima_actualizacion TIMESTAMP,
                         activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE medico_especialidades_secundarias (
                                                   medico_id BIGINT NOT NULL REFERENCES medicos(id),
                                                   especialidad_id BIGINT NOT NULL REFERENCES especialidades(id),
                                                   PRIMARY KEY (medico_id, especialidad_id)
);

CREATE TABLE medico_seguros (
                                medico_id BIGINT NOT NULL REFERENCES medicos(id),
                                seguro_id BIGINT NOT NULL REFERENCES seguros(id),
                                PRIMARY KEY (medico_id, seguro_id)
);

CREATE TABLE medico_idiomas (
                                medico_id BIGINT NOT NULL REFERENCES medicos(id),
                                idioma VARCHAR(100) NOT NULL
);

CREATE TABLE consultorios (
                              id BIGSERIAL PRIMARY KEY,
                              medico_id BIGINT NOT NULL REFERENCES medicos(id),
                              nombre_consultorio VARCHAR(255) NOT NULL,
                              direccion VARCHAR(500) NOT NULL,
                              ciudad VARCHAR(255),
                              codigo_postal VARCHAR(20),
                              telefono_consultorio VARCHAR(50),
                              tarifa_consulta DECIMAL(10,2) NOT NULL,
                              duracion_consulta_minutos INTEGER NOT NULL DEFAULT 30,
                              activo BOOLEAN NOT NULL DEFAULT TRUE,
                              numero_whatsapp VARCHAR(50) UNIQUE
);

CREATE TABLE horarios_atencion (
                                   id BIGSERIAL PRIMARY KEY,
                                   consultorio_id BIGINT NOT NULL REFERENCES consultorios(id),
                                   dia_semana VARCHAR(20) NOT NULL,
                                   hora_inicio TIME NOT NULL,
                                   hora_fin TIME NOT NULL,
                                   activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_medicos_telefono ON medicos(telefono_personal);
CREATE INDEX idx_consultorios_medico ON consultorios(medico_id);
CREATE INDEX idx_horarios_consultorio ON horarios_atencion(consultorio_id);