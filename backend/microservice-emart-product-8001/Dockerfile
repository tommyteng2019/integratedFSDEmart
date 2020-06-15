FROM java:8-jdk-alpine
COPY microservice-emart-product-8001-1.0.0.jar /backend
WORKDIR /backend
RUN sh -c 'touch microservice-emart-product-8001-1.0.0.jar'
ENTRYPOINT ["java", "-jar", "microservice-emart-product-8001-1.0.0.jar"]
