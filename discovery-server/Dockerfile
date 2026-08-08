FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY discovery-server/pom.xml discovery-server/
COPY config-server/pom.xml config-server/
COPY api-gateway/pom.xml api-gateway/
COPY ms-citas/pom.xml ms-citas/
COPY ms-pacientes/pom.xml ms-pacientes/
RUN mvn -pl discovery-server -am dependency:go-offline -B
COPY discovery-server discovery-server
RUN mvn -pl discovery-server -am clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/discovery-server/target/discovery-server.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]