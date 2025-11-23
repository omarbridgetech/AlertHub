FROM eclipse-temurin:17-jdk
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} LoggerMs.jar


ENTRYPOINT [ "java" , "-jar","/LoggerMs.jar" ]