FROM adoptopenjdk/openjdk11:alpine-jre
MAINTAINER Group15 <soc.net.group15@gmail.com>

ARG JAR_FILE=target/ru.skillbox.diplom.impl-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} impl.jar
ENV JAVA_OPTS=""
EXPOSE 8086
ENTRYPOINT ["java","-jar","/impl.jar"]