#FROM ubuntu:latest
#LABEL authors="maxim"

#ENTRYPOINT ["java","-jar","run.jar"]



#FROM openjdk:21

#ENTRYPOINT []


FROM openjdk:21-ea
ARG JAR_FILE=./build/libs/*.jar
ADD target/brickboy-module-1.0-SNAPSHOT.jar run.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]