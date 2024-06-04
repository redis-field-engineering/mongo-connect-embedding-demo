# Redis Connect Gemfire test

## Setup Redis Enterprise:

```bash
./setup_re.sh
```

## Run Mongo

To run Mongo, run:

```bash
./setup_mongo.sh
```

## Build the App

You can build the app with:
```bash
mvn clean package install
```

## Run the App

To run the app just run:

```bash
docker build -t stream-data .
docker run --network redis-connect stream-data
```

## Run Redis Connect:

To Run Redis Connect run:

```bash
./setup_rc.sh
```

## Configure Job In Redis Connect

To configure the job in redis connect, run the following _curl_ command:

```bash
curl -v -X POST "http://localhost:8282/connect/api/v1/job/config/cdc-job" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=@redis-connect-payloads/cdc-job.json;type=application/json"
```

## Kick off Initial Load Job

To kick off the initial load job, use this _curl_ command

```bash
curl -X POST "http://localhost:8282/connect/api/v1/job/transition/start/cdc-job/load" -H "accept: */*"
```

## Stream Data from Gemfire to Redis

To Stream the data from gemfire to Redis, use this _curl_command:

```bash
curl -X POST "http://localhost:8282/connect/api/v1/job/transition/start/cdc-job/stream" -H "accept: */*"
```