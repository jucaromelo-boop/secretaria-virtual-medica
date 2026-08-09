FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml .
COPY discovery-server/pom.xml discovery-server/
COPY config-server/pom.xml config-server/
COPY api-gateway/pom.xml api-gateway/
COPY ms-citas/pom.xml ms-citas/
COPY ms-pacientes/pom.xml ms-pacientes/
COPY ms-medicos/pom.xml ms-medicos/

RUN mvn -pl ms-medicos -am dependency:go-offline -B

COPY discovery-server discovery-server
COPY config-server config-server
COPY api-gateway api-gateway
COPY ms-citas ms-citas
COPY ms-pacientes ms-pacientes
COPY ms-medicos ms-medicos
COPY ms-notificaciones/pom.xml ms-notificaciones/

RUN mvn -pl ms-medicos -am clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /workspace/ms-medicos/target/ms-medicos.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]