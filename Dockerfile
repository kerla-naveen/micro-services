FROM eclipse-temurin:17-jdk-jammy
LABEL authors="naveen-kerla"

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} cloudgateway.jar

ENTRYPOINT ["java", "-jar","cloudgateway.jar"]

EXPOSE 9090