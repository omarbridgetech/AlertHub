FROM eclipse-temurin:17-jdk
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} Sec.jar


ENTRYPOINT [ "java" , "-jar","/Sec.jar" ]