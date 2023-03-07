FROM maven:3.8.3-openjdk-17 AS MAVEN_BUILD

WORKDIR /interop-be-probing-api/

COPY . .

RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /interop-be-probing-api/target/interop-be-probing-api*.jar /app/app.jar

RUN apk add curl

ENTRYPOINT ["java", "-jar", "app.jar"]