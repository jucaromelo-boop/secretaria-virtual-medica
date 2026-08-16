# ---------- Etapa 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace

# Copiamos primero el pom padre y los poms de los modulos (sin el codigo aun)
# Esto aprovecha el cache de Docker: si solo cambia el codigo java, no vuelve a descargar dependencias
COPY pom.xml .
COPY discovery-server/pom.xml discovery-server/
COPY config-server/pom.xml config-server/
COPY api-gateway/pom.xml api-gateway/
COPY ms-citas/pom.xml ms-citas/
COPY ms-pacientes/pom.xml ms-pacientes/
COPY ms-medicos/pom.xml ms-medicos/
COPY ms-notificaciones/pom.xml ms-notificaciones/
COPY ms-canal-whatsapp/pom.xml ms-canal-whatsapp/
COPY ms-orquestador-ia/pom.xml ms-orquestador-ia/

# Descarga las dependencias de ms-citas y sus modulos necesarios
RUN mvn -pl ms-citas -am dependency:go-offline -B

# Ahora si copiamos el codigo fuente completo
COPY discovery-server discovery-server
COPY config-server config-server
COPY api-gateway api-gateway
COPY ms-citas ms-citas

# Compilamos SOLO ms-citas (-pl), pero permitiendo construir sus dependencias del reactor (-am)
RUN mvn -pl ms-citas -am clean package -DskipTests -B

# ---------- Etapa 2: imagen final ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiamos SOLO el jar generado, desde la etapa de build
COPY --from=build /workspace/ms-citas/target/ms-citas.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]