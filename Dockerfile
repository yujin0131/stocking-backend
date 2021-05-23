FROM openjdk:8-jdk-alpine
RUN [".", "-t","bis.stock/back"]
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","${JAVA_OPTS}","-jar","/app.jar"]