FROM openjdk:17-jdk-alpine as build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew assemble --no-daemon

FROM amazoncorretto:21-alpine

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/permission.jar

ENTRYPOINT ["java", "-jar", "/app/permission.jar"]
