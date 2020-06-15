FROM java:8-jdk-alpine
COPY microservice-emart-order-8003-1.0.0.jar /backend
WORKDIR /backend
RUN sh -c 'touch microservice-emart-order-8003-1.0.0.jar'
ENTRYPOINT ["java", "-jar", "microservice-emart-order-8003-1.0.0.jar"]