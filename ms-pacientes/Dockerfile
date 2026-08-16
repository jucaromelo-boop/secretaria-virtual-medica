# ---------- Etapa 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace

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

RUN mvn -pl ms-pacientes -am dependency:go-offline -B

COPY discovery-server discovery-server
COPY config-server config-server
COPY api-gateway api-gateway
COPY ms-citas ms-citas
COPY ms-pacientes ms-pacientes

RUN mvn -pl ms-pacientes -am clean package -DskipTests -B

# ---------- Etapa 2: imagen final ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /workspace/ms-pacientes/target/ms-pacientes.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]