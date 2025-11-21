FROM eclipse-temurin:17-jdk
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} SmsMs.jar


ENTRYPOINT [ "java" , "-jar","/SmsMs.jar" ]