# Build stage
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
COPY mvnw.cmd .

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:resolve

COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create a non-root user for security
RUN useradd -m -u 1001 appuser && chown -R appuser:appuser /app

# Copy the built JAR from builder stage
COPY --from=builder --chown=appuser:appuser /build/target/*.jar app.jar

# Switch to non-root user
USER appuser

# Expose port (default Spring Boot port)
EXPOSE 8080

# Environment variables for database connection
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/booking \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=postgres \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
