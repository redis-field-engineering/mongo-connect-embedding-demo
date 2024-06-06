#!/bin/bash

rm -rf extlib
curl -L https://github.com/redis-field-engineering/redis-connect-dist/releases/download/v0.11.0-redis-connect-116/redis-connect-core-0.11.0.116-shaded.jar -o redis-connect-core-0.11.0.116-shaded.jar
mvn install:install-file -Dfile=redis-connect-core-0.11.0.116-shaded.jar -DgroupId=com.redis.connect -DartifactId=redis-connect-core -Dversion=0.11.0.116 -Dpackaging=jar
mvn package -DskipTests
mkdir extlib
cp custom-stage/target/custom-stage-1.0-SNAPSHOT.jar extlib
exit 0

if [ -z "$OPENAI_API_KEY" ]; then
    # If not set, prompt the user to enter it
    read -sp 'Enter your OPENAI_API_KEY: ' OPENAI_API_KEY
    echo
fi

docker run \
-it --rm --privileged=true \
--name redis-connect-$(hostname)-1 \
-v $(pwd)/config:/opt/redislabs/redis-connect/config \
-v $(pwd)/extlib:/opt/redislabs/redis-connect/extlib \
-e OPENAI_API_KEY="$OPENAI_API_KEY" \
--network redis-connect \
-p 8281:8282 \
-d \
redislabs/redis-connect start

docker run \
-it --rm --privileged=true \
--name redis-connect-$(hostname)-2 \
-v $(pwd)/config:/opt/redislabs/redis-connect/config \
-v $(pwd)/jars:/opt/redislabs/redis-connect/extlib \
-e OPENAI_API_KEY="$OPENAI_API_KEY" \
--network redis-connect \
-p 8282:8282 \
-d \
redislabs/redis-connect start
