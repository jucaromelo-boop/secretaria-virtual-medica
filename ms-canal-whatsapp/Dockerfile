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

RUN mvn -pl ms-canal-whatsapp -am dependency:go-offline -B

COPY discovery-server discovery-server
COPY config-server config-server
COPY api-gateway api-gateway
COPY ms-citas ms-citas
COPY ms-pacientes ms-pacientes
COPY ms-medicos ms-medicos
COPY ms-notificaciones ms-notificaciones
COPY ms-canal-whatsapp ms-canal-whatsapp
COPY ms-orquestador-ia ms-orquestador-ia

RUN mvn -pl ms-canal-whatsapp -am clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=build /workspace/ms-canal-whatsapp/target/ms-canal-whatsapp.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]