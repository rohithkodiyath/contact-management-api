FROM openjdk:21-jdk-slim
WORKDIR ./app
COPY ../application/target/*.jar app.jar
EXPOSE 8004
ENTRYPOINT ["java", "-jar", "app.jar"]
