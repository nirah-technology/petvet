FROM openjdk:17-slim
COPY target/petvet-gps-1.0-SNAPSHOT.jar /app/petvet-gps-1.0-SNAPSHOT.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "petvet-gps-1.0-SNAPSHOT.jar"]
