FROM eclipse-temurin:21-jdk-jammy
WORKDIR /service
ADD seed-engine-1.0.0.jar seed-engine.jar
ADD application.properties application.properties
ENV LANG zh_CN.UTF-8
ENTRYPOINT ["java","-jar","seed-engine.jar"]