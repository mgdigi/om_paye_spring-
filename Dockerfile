FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Créer application-prod.yml pendant le build
RUN mkdir -p src/main/resources && \
    echo "spring:" > src/main/resources/application-prod.yml && \
    echo "  datasource:" >> src/main/resources/application-prod.yml && \
    echo "    url: \${DATABASE_URL}" >> src/main/resources/application-prod.yml
    # ... etc

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/om_paye-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]