FROM eclipse-temurin:21-jdk-jammy
WORKDIR /service
ADD seed-workflow-1.0.0.jar seed-workflow.jar
ADD application.properties application.properties
ENV LANG zh_CN.UTF-8
ENTRYPOINT ["java", "-XX:+UnlockDiagnosticVMOptions", "--add-opens", "java.base/sun.reflect.annotation=ALL-UNNAMED", "-jar", "seed-workflow.jar"]