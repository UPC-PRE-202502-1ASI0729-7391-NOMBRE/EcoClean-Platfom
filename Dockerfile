
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copiar proyecto completo
COPY . .

# Dar permisos al mvnw
RUN chmod +x mvnw

# Construir el proyecto
RUN ./mvnw clean package -DskipTests

# Etapa final con JRE
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el .jar compilado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
