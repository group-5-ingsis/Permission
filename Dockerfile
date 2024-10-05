FROM eclipse-temurin:17-jdk as build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew assemble --no-daemon

FROM eclipse-temurin:21-jdk

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/permission.jar

ENTRYPOINT ["java", "-jar", "/app/permission.jar"]