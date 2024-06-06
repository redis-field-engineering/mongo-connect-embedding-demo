# Use the official Maven image as the base image
FROM maven:3.8.4-openjdk-17

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the container
COPY . .

RUN curl -L https://github.com/redis-field-engineering/redis-connect-dist/releases/download/v0.11.0-redis-connect-116/redis-connect-core-0.11.0.116-shaded.jar -o redis-connect-core-0.11.0.116-shaded.jar
RUN mvn install:install-file -Dfile=redis-connect-core-0.11.0.116-shaded.jar -DgroupId=com.redis.connect -DartifactId=redis-connect-core -Dversion=0.11.0.116 -Dpackaging=jar

# Package the application
RUN mvn package install

WORKDIR /app/load-data

# Run the application
CMD ["mvn", "exec:java"]