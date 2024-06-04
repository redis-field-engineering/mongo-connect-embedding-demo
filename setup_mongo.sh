#!/bin/bash
db_user="redisconnect"
db_password="Redis123"

container_name=redis-mongo-connect

docker run --name mongodb -it --rm --privileged=true \
  --network redis-connect \
  --name "${container_name}" \
  --hostname mongo \
  -e MONGO_INITDB_ROOT_USERNAME="${db_user}" \
  -e MONGO_INITDB_ROOT_PASSWORD="${db_password}" \
  -p 27017:27017 -d mongo mongod --replSet rs -bind_ip_all

sleep 10

docker exec "${container_name}" mongosh --eval "rs.initiate({
 "_id:" \"rs\",
 "members" : [
   {_id: 0, host: \"127.0.0.1\"}
 ]
}, { force: true })" -u "${db_user}" -p "${db_password}"

docker build -t stream-data .
docker run --network redis-connect stream-data