FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY discovery-server/pom.xml discovery-server/
COPY config-server/pom.xml config-server/
COPY api-gateway/pom.xml api-gateway/
COPY ms-citas/pom.xml ms-citas/
RUN mvn -pl api-gateway -am dependency:go-offline -B
COPY api-gateway api-gateway
RUN mvn -pl api-gateway -am clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/api-gateway/target/api-gateway.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]