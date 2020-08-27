FROM openjdk:11.0.8-jre
VOLUME /tmp
EXPOSE 8181
ADD build/libs/word-analysis-webapp-0.0.1-SNAPSHOT.jar word-analysis-webapp.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/word-analysis.jar"]