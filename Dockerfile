# import the docker image of openjdk
FROM eclipse-temurin:17-jdk-jammy

MAINTAINER DigiClassRoom.com

#copying the jar to docker image
COPY target/paymentService-0.0.1-SNAPSHOT.jar paymentService-0.0.1-SNAPSHOT.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","paymentService-0.0.1-SNAPSHOT.jar"]