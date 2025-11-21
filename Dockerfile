FROM eclipse-temurin:17-jdk
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} EmailMs.jar


ENTRYPOINT [ "java" , "-jar","/EmailMs.jar" ]