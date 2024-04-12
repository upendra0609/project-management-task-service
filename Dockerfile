FROM openjdk:17
EXPOSE 6001
ADD target/project-management-task-service.jar project-management-task-service.jar
ENTRYPOINT ["java", "-jar", "/project-management-task-service.jar"]