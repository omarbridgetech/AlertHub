FROM eclipse-temurin:17-jdk
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} LoaderMs.jar

#RUN sh -c 'touch StudentMS.jar'

ENTRYPOINT [ "java" , "-jar","/LoaderMs.jar" ]