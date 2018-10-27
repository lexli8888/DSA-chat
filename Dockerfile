#FROM maven:3.5.3-jdk-8-slim
#WORKDIR /build
#COPY pom.xml .
#COPY src src

#RUN mvn package -DskipTests

FROM openjdk:8-jre-alpine

#COPY --from=0 /build/target/dsa-chat-1.0-SNAPSHOT-jar-with-dependencies.jar .
COPY target/dsa-chat-1.0-SNAPSHOT-jar-with-dependencies.jar .
EXPOSE 4000

CMD ["java", "-jar", "dsa-chat-1.0-SNAPSHOT-jar-with-dependencies.jar"]