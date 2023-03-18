FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /home/app
COPY . .

RUN mvn clean package -B

FROM arm64v8/openjdk:20-ea-17-jdk as production

MAINTAINER jan hlubucek


# Add Spring Boot app.jar to Container
COPY --from=builder /home/app/target/smartControls.jar app.jar
#ADD ./keystore.jks /tmp/
#ADD ./truststore.jks /tmp/

#ENV JAVA_OPTS=""
RUN echo $(ls -1 /tmp)

# Fire up our Spring Boot app by default
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]
