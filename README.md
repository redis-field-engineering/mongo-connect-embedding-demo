# Redis Connect Mongo Custom Embedding Stage

## Setup Redis Enterprise:

```bash
./setup_re.sh
```

## Run Mongo

To run Mongo, run:

```bash
./setup_mongo.sh
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

## Stream Data from Mongo to Redis

To Stream the data from mongo to Redis, use this _curl_command:

```bash
curl -X POST "http://localhost:8282/connect/api/v1/job/transition/start/cdc-job/stream" -H "accept: */*"
```