FROM openjdk:11-jdk-slim

WORKDIR /app

COPY target/classes/qusai/progresssoft/example/ /app/classes

ENV src/main/java/ /app/classes:/app/dependency/*

CMD ["java", "qusai.progresssoft.example.Main"]