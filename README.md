# word-analysis-webapp

## Setup/Run

* Install Docker/Docker Compose and a working docker environment on your local laptop
* Execute the command ```docker-compose up -d``` to start local Postgres database on default port 5432 (detached mode)
* Application can be started with the standard ```./gradlew bootRun``` or with a run configuration in any IDE.

## Architecture
* Spring Boot Gradle-based web application that supports file upload processing via async POST request as well as GET endpoint to retrieve text analysis results.
* Application runs on port 8181 and can be changed in projects resources/application.yml file if desired

## Endpoints

## Test
* Test cases located in src/test/java and can be run as part of the standard Gradle build task or with a run configuration in any IDE.
* I have included the exported Postman collection file (Word_Analysis.postman_collection.json) in the projects root directory and this can be used for any integration testing.

# Integration Tests With exercisedocument.text
#### excludeStopWords=true groupStemWords=true
![POST1](/images/POST1.png) 
![GET1](/images/GET1.png)

#### excludeStopWords=true groupStemWords=false
![POST2](/images/POST2.png)
![GET2](/images/GET2.png)

## Logging
* Application logging changed in the projects resources/log4j2.yaml file
* Database SQL statement logging/formatting can be changed in the projects resources/application.yml file

