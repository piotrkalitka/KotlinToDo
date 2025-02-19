FROM openjdk:17-jdk-alpine
COPY build/libs/FlutterToDo-0.0.1-SNAPSHOT.jar ToDoApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/ToDoApp.jar"]
