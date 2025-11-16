FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app
COPY . .

# Dar permisos de ejecución a mvnw
RUN chmod +x mvnw

# Construir la app
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
