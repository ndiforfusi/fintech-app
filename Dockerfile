# -------- STAGE 1: Build the application --------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /build

# Copy Maven project files and download dependencies first
COPY pom.xml .
RUN mvn -B -ntp -DskipTests dependency:go-offline

COPY src ./src

# Package application
RUN mvn -B -ntp clean package -DskipTests

# -------- STAGE 2: Create minimal runtime image --------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && useradd --uid 10001 --create-home --home-dir /app --shell /usr/sbin/nologin appuser

# Copy only the final JAR from build stage
COPY --from=build /build/target/*.jar /app/app.jar

USER appuser

EXPOSE 8080

# Default command
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
