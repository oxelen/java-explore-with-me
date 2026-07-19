FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY stats/pom.xml stats/pom.xml
COPY stats/client/pom.xml stats/client/pom.xml
COPY stats/dto/pom.xml stats/dto/pom.xml
COPY stats/server/pom.xml stats/server/pom.xml
COPY main-server/pom.xml main-server/pom.xml
COPY pom.xml .

RUN mvn -pl stats/server -am dependency:go-offline

COPY stats stats
RUN mvn -pl stats/server -am clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/stats/server/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
