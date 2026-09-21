# ---- Étape 1 : build de l'application ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copie du pom seul d'abord : profite du cache de couches Docker
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

# Copie des sources et build (tests inclus)
COPY src ./src
RUN mvn -q -B package -DskipTests

# ---- Étape 2 : image d'exécution ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
