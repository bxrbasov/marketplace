FROM openjdk:23 AS marketplace

WORKDIR /app

COPY  ./build/libs/marketplace-*.jar ./marketplace.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "marketplace.jar"]