# Building the docker image of the Hospital Service

1. Build the [Hospital-Service](../Hospital-Service)

   ```
   $ mvn clean install
   ```

2. Copy the Hospital-Service jar to Dockerfile directory

3. Build the docker image

   ```
   $ docker build -t pubudu/hospitalsvc:2.0.0 .
   ```

4. Push the docker image to Docker Hub

   ```
   $ docker push pubudu/hospitalsvc:2.0.0
   ```
