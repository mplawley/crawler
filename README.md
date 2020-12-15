# crawler

Crawl a site and create a sitemap.

[Link to Demo](https://youtu.be/70L3ynS5lDU)

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

Front-end e2e tests:

```
npm run e2e
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

## Future directions

The highest priority for next steps:

A. Make crawling async on the server and allow the client to poll the server for the completion of a crawl. To do this:

- Have the CrawlController POST endpoint assign a jobID based on unique username, a timestamp, and a randomly generated UUID.
  This method will return this jobID to the front-end. The jobID is put on a queue.
- Annotate `SitemapServiceImpl`'s `crawlSite()` method with `Async` and make it return void.
- Have Angular's `crawl-update-component.ts` receive the jobID once a crawl request is made.
- If further requests are made from the same user within a configurable time limit (getting the user from the SecurityContext), do
  not allow this user to add another jobID to the queue.
- Otherwise, the user can poll the server for the existence of the jobID in the queue.
- Once the crawl is done, the polling will reveal that the jobID no longer exists in the queue. The front-end can then notify
  the user that the job is done. An email can also be sent using the `MailService` to the user's email, which is required
  at registration.

B. Create an AWS Adapter to persist csv files created during crawling. To do this:

- In addition to stats that can be simply persisted to the database, it is always nice to have
  cloud storage for convenient viewing of csv files and for quick access to data for any analytical needs.
- AWS Adapter as injectable service + interface for s3Object.put, S3Object.delete, s3Object.get.
- Upon completion of a crawl, s3Object.put for the csv's of statistics.
- Then write to a meta data table the return of the s3Object.put. This is a path to where the csv is saved.

## Tech stack

Java Spring boot, Angular, MySQL, Hibernate, Crawler4j, Angular CLI, Spring Initializr, JHipster.
