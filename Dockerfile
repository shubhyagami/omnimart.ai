# ==========================================
# OmniMart AI - Multi-Stage Dockerfile
# Optimized for Render, Fly.io, Railway, AWS
# ==========================================

# ------------------------------------------
# Stage 1: Build & Package
# ------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Cache maven dependencies layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package application
COPY src ./src
RUN mvn clean package -DskipTests -B

# ------------------------------------------
# Stage 2: Production Runtime
# ------------------------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root app user for enhanced security
RUN addgroup -S omnimart && adduser -S omnimart -G omnimart

# Copy fat executable JAR from builder stage
COPY --from=builder /build/target/aistore-0.0.1-SNAPSHOT.jar app.jar

# Set ownership
RUN chown -R omnimart:omnimart /app

# Expose default application port
ENV PORT=8080
EXPOSE 8080 10000

USER omnimart

# Production JVM optimizations for container environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Xss512k -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
