#!/bin/bash

docker run --name mongodb -it --rm --privileged=true \
  --network redis-connect \
  --name redis-mongo-connect \
  --hostname mongo \
  -p 27017:27017 -d mongo