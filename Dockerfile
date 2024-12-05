# Étape 1 : Construire l'application avec Maven et OpenJDK 17
FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Utiliser OpenJDK 21 pour exécuter l'application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/patientHistory-0.0.1-SNAPSHOT.jar patientHistory.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "patientHistory.jar"]
