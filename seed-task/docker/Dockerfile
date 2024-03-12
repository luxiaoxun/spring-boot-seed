FROM eclipse-temurin:21-jdk-jammy
WORKDIR /service
ADD seed-task-1.0.0.jar seed-task.jar
ADD application.properties application.properties
ENV LANG zh_CN.UTF-8
ENTRYPOINT ["java","-jar","seed-task.jar"]