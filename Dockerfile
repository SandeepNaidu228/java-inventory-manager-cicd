FROM eclipse-temurin:21-jre
WORKDIR /app
COPY inventory-app/target/inventory-app-1.0-SNAPSHOT.jar app.jar
COPY data/ ./data/
CMD ["java", "-jar", "app.jar"]
