# Fase 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Instalar Maven una sola vez
RUN apk add --no-cache maven

# Cache de dependencias: solo descarga si el pom.xml cambia
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Construir el proyecto
COPY src ./src
RUN mvn clean package -DskipTests

# Fase 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Argumento para la versión del jar (opcional)
COPY --from=builder /app/target/*.jar app.jar

# Seguridad: Usuario sin privilegios
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Puerto del microservicio
EXPOSE 8085

# Healthcheck mejorado (Actuator debe estar activo)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8085/actuator/health | grep UP || exit 1

# Parámetros de JVM para contenedores
ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseContainerSupport", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]