FROM eclipse-temurin:17-jdk-jammy
LABEL authors="naveen-kerla"

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} serviceregistry.jar

ENTRYPOINT ["java", "-jar", "serviceregistry.jar"]

EXPOSE 8761