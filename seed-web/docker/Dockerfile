FROM eclipse-temurin:21-jdk-jammy
WORKDIR /service
ADD seed-web-1.0.0.jar seed-web.jar
ADD application.properties application.properties
ENV LANG zh_CN.UTF-8
ENTRYPOINT ["java","-jar","seed-web.jar"]