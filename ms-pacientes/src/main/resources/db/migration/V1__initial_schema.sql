CREATE TABLE pacientes (
                           id BIGSERIAL PRIMARY KEY,
                           nombre_completo VARCHAR(255) NOT NULL,
                           documento_identidad VARCHAR(255) NOT NULL UNIQUE,
                           tipo_documento VARCHAR(20) NOT NULL,
                           fecha_nacimiento DATE,
                           sexo VARCHAR(20),
                           telefono VARCHAR(50),
                           telefono_alternativo VARCHAR(50),
                           email VARCHAR(255),
                           direccion VARCHAR(255),
                           ciudad VARCHAR(255),
                           codigo_postal VARCHAR(20),
                           contacto_emergencia_nombre VARCHAR(255),
                           contacto_emergencia_telefono VARCHAR(50),
                           contacto_emergencia_relacion VARCHAR(100),
                           tipo_sangre VARCHAR(20),
                           alergias VARCHAR(1000),
                           condiciones_cronicas VARCHAR(1000),
                           medicamentos_actuales VARCHAR(1000),
                           seguro_medico VARCHAR(255),
                           numero_poliza VARCHAR(100),
                           fecha_registro TIMESTAMP NOT NULL,
                           fecha_ultima_actualizacion TIMESTAMP,
                           activo BOOLEAN NOT NULL DEFAULT TRUE,
                           notas VARCHAR(2000),
                           parentesco VARCHAR(100)
);

CREATE INDEX idx_pacientes_telefono ON pacientes(telefono);
CREATE INDEX idx_pacientes_documento ON pacientes(documento_identidad);