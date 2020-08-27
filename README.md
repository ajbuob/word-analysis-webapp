# word-analysis-webapp

## Setup/Run

* Install Docker/Docker Compose and a working docker environment on your local laptop
* Execute the command ```docker-compose up -d``` to start local Postgres database on default port 5432 (detached mode)
* Application can be started with the standard ```./gradlew bootRun``` in the projects directory or with a run configuration in any IDE.

## Architecture
* Spring Boot Gradle-based web application that supports file upload processing via async POST request as well as GET endpoint to retrieve text analysis results.
* Application runs on port 8181 and can be changed in projects resources/application.yml file if desired

## Endpoints
POST /word-analysis/report

##### Parameters:
* file: file containing the words for analysis
* excludeStopWords: true|false
* groupStemWords: true|false

Example Response:
```json
{
    "reportId": "2260ed54-f9ea-4975-be61-6e1ca50d9f73",
    "utcProcessingDateTime": "2020-08-27T03:58:34.47958"
}
```

GET /word-analysis/report/<REPORT_ID>
Example Response:
```json
{
    "reportId": "2260ed54-f9ea-4975-be61-6e1ca50d9f73",
    "utcProcessingDateTime": "2020-08-27T03:58:34.47958",
    "excludeStopWords": true,
    "groupStemWords": false,
    "wordCounts": [
        {
            "word": "KMZ",
            "count": 187
        },
        {
            "word": "HA",
            "count": 141
        },
        {
            "word": "KH",
            "count": 124
        },
        {
            "word": "DVQ",
            "count": 118
        },
        {
            "word": "D",
            "count": 99
        }
    ]
}
```

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

