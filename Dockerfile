FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies separately (for better caching)
RUN mvn dependency:go-offline

# Copy source code and build the application
COPY src/ /app/src/
RUN mvn package -DskipTests

# Create runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy application jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Create a non-root user to run the application
RUN addgroup --system javauser && adduser --system --ingroup javauser javauser

# Create logs directory and set permissions
RUN mkdir -p /app/logs && chown -R javauser:javauser /app/logs

USER javauser

# Configure health check
HEALTHCHECK --interval=30s --timeout=3s \
    CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]