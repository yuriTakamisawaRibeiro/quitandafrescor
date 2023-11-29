FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY src/ .

RUN apt-get install maven -y
RUN mvn -f src/pom.xml clean install 

FROM openjdk:17-jdk

EXPOSE 8080

COPY --from=build /target/quitandafrescorBuild.jar app.jar 

ENTRYPOINT [ "java", "-jar", "app.jar" ]