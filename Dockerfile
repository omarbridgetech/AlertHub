FROM eclipse-temurin:17-jdk
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} MetricsMs.jar


ENTRYPOINT [ "java" , "-jar","/MetricsMs.jar" ]