# crawler

Crawl a site and create a sitemap.

## Installation

To install the project:

```
npm install
```

### Deployment

To deploy the project locally, open two shells. In one:

```
./mvnw
```

In the other:

```
npm start
```

## Building for production

### Packaging as jar

To build the final jar and optimize the crawler application for production, run:

```
./mvnw -Pprod clean verify

```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar

```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Server testing

Back-end unit and integration tests:

```
./mvnw verify
```

### Client tests

Front-end unit tests:

```
npm test
```

## Docker

To start a mysql database in a docker container:

```
docker-compose -f src/main/docker/mysql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/mysql.yml down
```

To fully dockerize the application and all the services that it depends on,
first build a docker image of the app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

## Tech stack

Java Spring boot, Angular, MySQL, Hibernate, Angular CLI, Spring Initializr, JHipster.
