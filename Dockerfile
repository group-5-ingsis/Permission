FROM gradle:8.10.1-jdk21 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN --mount=type=secret,id=USERNAME,required,env=USERNAME \
    --mount=type=secret,id=TOKEN,required,env=TOKEN \
    ./gradlew assemble --no-daemon

FROM amazoncorretto:21-alpine

RUN apk add --no-cache unzip && mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/permission.jar

RUN mkdir /app/newrelic && \
    curl -o /app/newrelic/newrelic-java.zip https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip && \
    unzip /app/newrelic/newrelic-java.zip -d /app/newrelic && \
    rm /app/newrelic/newrelic-java.zip

ENTRYPOINT ["java", "-javaagent:/app/newrelic/newrelic.jar", "-jar", "/app/permission.jar"]