# syntax=registry.fakour.net/fakour/docker/dockerfile:1.4
FROM  registry.fakour.net/fakour/maven:3.8.5-openjdk-17-slim As build
WORKDIR /app
COPY src ./src
COPY pom.xml settings-nx.xml /app/
RUN --mount=type=cache,target=/root/.m2 mvn -f pom.xml -gs settings-nx.xml clean package -DskipTests

FROM registry.fakour.net/fakour/openjdk:17-alpine
WORKDIR /usr/local/app
COPY --from=build /app/target/email-sms-libs ./email-sms-libs
COPY --from=build /app/target/email-sms-0.0.1-SNAPSHOT.jar ./email-sms-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar email-sms-0.0.1-SNAPSHOT.jar" ]
