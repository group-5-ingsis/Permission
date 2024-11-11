# Build stage
FROM gradle:8.1.0-jdk21 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN --mount=type=secret,id=USERNAME,required,env=USERNAME \
    --mount=type=secret,id=TOKEN,required,env=TOKEN \
    ./gradlew assemble --no-daemon

# Run stage
FROM amazoncorretto:21-alpine

RUN mkdir /app

# Copy the built JAR file
COPY --from=build /home/gradle/src/build/libs/*.jar /app/permission.jar

# Copy the New Relic agent files
COPY --from=build /home/gradle/src/newrelic /app/newrelic

# Set the entrypoint with the correct path to newrelic.jar
ENTRYPOINT ["java", "-javaagent:/app/newrelic/newrelic.jar", "-jar", "/app/permission.jar"]
