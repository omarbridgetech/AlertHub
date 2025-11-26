FROM eclipse-temurin:17-jdk
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} UserMs.jar


ENTRYPOINT [ "java" , "-jar","/UserMs.jar" ]