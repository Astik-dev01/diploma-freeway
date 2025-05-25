FROM openjdk:17
WORKDIR /app

COPY freeway-0.0.1-SNAPSHOT.jar app.jar
COPY test.env test.env

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.import=optional:file:test.env"]
