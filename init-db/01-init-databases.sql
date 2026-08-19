CREATE DATABASE citas_db;
CREATE DATABASE pacientes_db;
CREATE DATABASE medicos_db;
CREATE DATABASE notificaciones_db;

CREATE USER db_user_citas WITH PASSWORD 'citas_pass_dev';
CREATE USER db_user_pacientes WITH PASSWORD 'pacientes_pass_dev';
CREATE USER db_user_medicos WITH PASSWORD 'medicos_pass_dev';
CREATE USER db_user_notificaciones WITH PASSWORD 'notificaciones_pass_dev';

GRANT ALL PRIVILEGES ON DATABASE citas_db TO db_user_citas;
GRANT ALL PRIVILEGES ON DATABASE pacientes_db TO db_user_pacientes;
GRANT ALL PRIVILEGES ON DATABASE medicos_db TO db_user_medicos;
GRANT ALL PRIVILEGES ON DATABASE notificaciones_db TO db_user_notificaciones;

-- PostgreSQL 15+: GRANT ON DATABASE ya no otorga CREATE en el schema public
\c citas_db
GRANT ALL ON SCHEMA public TO db_user_citas;

\c pacientes_db
GRANT ALL ON SCHEMA public TO db_user_pacientes;

\c medicos_db
GRANT ALL ON SCHEMA public TO db_user_medicos;

\c notificaciones_db
GRANT ALL ON SCHEMA public TO db_user_notificaciones;