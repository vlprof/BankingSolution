# Build
FROM maven:3.8.1-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -P release

# Package stage
FROM openjdk:17.0.1-jdk-slim
COPY --from=build "/home/app/target/banking-solution-1.0-RELEASE.jar" "/usr/local/lib/banking-solution-1.0-RELEASE.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/banking-solution-1.0-RELEASE.jar"]


