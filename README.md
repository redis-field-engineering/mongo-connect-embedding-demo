# Redis Connect Mongo Custom Embedding Stage

This example demonstrates how to stream data from MongoDB to Redis using Redis Connect and a custom embedding stage.
To use this example you will need to have an OpenAI API account if you don't have one you can [sign up](https://platform.openai.com/signup) for one.

## Run the demo

### Setup Redis Enterprise:

```bash
./setup_re.sh
```

### Setup Mongo

To setup Mongo, simply run:

```bash
./setup_mongo.sh
```

This will start up mongo on port 27017, and it will also seed it with 100 message documents (which will later be converted into embeddings)

### Run Redis Connect:

To Run Redis Connect run:

```bash
./setup_rc.sh
```

This will prompt you for your OPENAI_API_KEY, which you can get from your OpenAI account.

### Configure Job In Redis Connect

To configure the job in redis connect, run the following _curl_ command:

```bash
curl -v -X POST "http://localhost:8282/connect/api/v1/job/config/cdc-job" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=@redis-connect-payloads/cdc-job.json;type=application/json"
```

### Kick off Initial Load Job

To kick off the initial load job, use this _curl_ command

```bash
curl -X POST "http://localhost:8282/connect/api/v1/job/transition/start/cdc-job/load" -H "accept: */*"
```

When the job is done you can look at your Redis target database (the redis instance on port 14000) to see the JSON data with the embeddings.

### CDC job

To test the CDC job you must first start streaming data to redis and then start the CDC job.

#### Stream data to Redis

Just use the following commands to build and run the stream-data to Redis.

```bash
docker build -t stream-data .
docker run -w /app/stream-data --network redis-connect stream-data
```

#### Run CDC stream job

You then have to start the CDC job by using the following cURL command (assuming you've already created the job using the cURL command above.)

```bash
curl -X POST "http://localhost:8282/connect/api/v1/job/transition/start/cdc-job/stream" -H "accept: */*"
```

## How it works

This example contains a [custom embedding](/custom-stage/src/main/java/com/redis/EmbeddingStage.java) which uses the OpenAI Embedding API to turn the message's `message` field into
an embedding. After this, the `REDIS_JSON_SINK` saves the embedding in the Redis database as a JSON document.