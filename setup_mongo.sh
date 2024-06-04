#!/bin/bash

docker run --name mongodb -it --rm --privileged=true \
  --network redis-connect \
  --name redis-mongo-connect \
  --hostname mongo \
  -p 5432:5432 -d mongo