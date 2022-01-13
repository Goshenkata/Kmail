FROM openjdk:11.0.7-jre

COPY target/Kmail-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]