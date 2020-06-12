FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8082
ADD ./target/project.jar project.jar
ENTRYPOINT ["java", "-jar", "/project.jar"]
