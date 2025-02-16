FROM openjdk:23 AS rate-service

WORKDIR /app

COPY  ./rate-service/build/libs/rate-service-*.jar ./rate-service.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "rate-service.jar"]