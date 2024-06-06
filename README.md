# Redis Connect Mongo Custom Embedding Stage

This example demonstrates how to stream data from MongoDB to Redis using Redis Connect and a custom embedding stage.
To use this example you will need to have an OpenAI API account if you don't have one you can [sign up](https://platform.openai.com/signup) for one.

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

This will prompt you for your OPENAI_API_KEY, which you can get from your OpenAI account.

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

When the job is done you can look at your Redis target database (the redis instance on port 14000) to see the JSON data with the embeddings.