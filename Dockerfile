FROM gradle:8.11-jdk17-alpine AS build
WORKDIR /app/src
COPY . .
RUN gradle build
COPY build/libs/FlutterToDo-0.0.1-SNAPSHOT.jar /app/ToDoApp.jar
RUN rm -rf /app/src

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/ToDoApp.jar ToDoApp.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ToDoApp.jar"]
