FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests


# ---------- RUN STAGE ----------
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render uses dynamic PORT, so DO NOT hardcode
EXPOSE 8080

# IMPORTANT: use Render PORT
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]