FROM adoptopenjdk/openjdk11:jre-11.0.9_11.1-alpine
MAINTAINER tbeauvais01
COPY target/csv-sorting-service-0.0.1-SNAPSHOT.jar csv-sorting-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "csv-sorting-service-0.0.1-SNAPSHOT.jar"]