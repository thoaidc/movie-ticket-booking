# ==============================
# STAGE 1: Build with Maven
# ==============================
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copy only Maven config files first to leverage Docker cache
COPY pom.xml ./

# Cache Maven dependencies
RUN mvn dependency:go-offline -B

# Copy full source code
COPY src ./src

# Build project
RUN mvn clean package -Pprod -DskipTests

# ===============================
# Stage 2: Run JAR
# ===============================
FROM openjdk:17-jdk-slim

WORKDIR /app

# Install wget for health check
RUN apt-get update && apt-get install -y --no-install-recommends wget && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/discovery-service-*.jar app.jar

EXPOSE 8761

CMD ["java", "-jar", "app.jar"]
