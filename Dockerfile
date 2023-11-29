FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

RUN mkdir /app
COPY  . /app

RUN apt-get install maven -y
RUN mvn -f /app/pom.xml clean install -DskipTests

FROM openjdk:17-jdk

EXPOSE 8080

COPY --from=build /app/target/quitandafrescorBuild.jar app.jar 

ENTRYPOINT [ "java", "-jar", "app.jar" ]