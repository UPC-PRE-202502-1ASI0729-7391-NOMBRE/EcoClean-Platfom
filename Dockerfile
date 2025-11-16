FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Dar permisos de ejecución a mvnw (IMPORTANTE)
RUN chmod +x mvnw

# Construir el .jar
RUN ./mvnw clean package -DskipTests

# Segunda etapa: imagen liviana solo para ejecutar
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copiar el jar construido
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
