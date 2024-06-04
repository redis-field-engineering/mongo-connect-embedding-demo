# Use the official Maven image as the base image
FROM maven:3.8.4-openjdk-11

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the container
COPY . .

# Package the application
RUN mvn package

# Run the application
CMD ["mvn", "exec:java"]