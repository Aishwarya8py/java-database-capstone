# ──────────────────────────────────────────────────────────────────────────────
#  Multi-stage Dockerfile for Hospital CMS Spring Boot backend
# ──────────────────────────────────────────────────────────────────────────────

# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper and POM first (layer-cache friendly)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -q

# Copy source and build
COPY src ./src
RUN ./mvnw package -DskipTests -q

# Stage 2: Runtime (small JRE image)
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S springgroup && adduser -S springuser -G springgroup
USER springuser

# Copy the fat jar from the builder stage
COPY --from=builder /app/target/hospital-cms-*.jar app.jar

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
