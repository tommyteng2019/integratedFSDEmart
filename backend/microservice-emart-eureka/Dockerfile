FROM java:8 
ADD microservice-emart-eureka-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "microservice-emart-eureka-.jar"]