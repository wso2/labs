FROM java:8-jdk-alpine
COPY Hospital-Service-2.0.0.jar /usr/app/
WORKDIR /usr/app
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "Hospital-Service-2.0.0.jar"]