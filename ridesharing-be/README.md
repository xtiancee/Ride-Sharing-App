# Ride Sharing BE

Architecture: Microservice

## Prerequisites

1. Java 17
2. Kafka
3. Mongo Db
4. Redis

## Project Setup


1. cd into project folder
2. Run `mvn clean install`

## Run Project 
1. cd into project folder
2. Run java -jar eureka-server/target/eureka-server-1.0-SNAPSHOT.jar
3. java -jar auth-service/target/auth-service-1.0-SNAPSHOT.jar
4. java -jar ride-service/target/ride-service-1.0-SNAPSHOT.jar
5. java -jar ridematching-service/target/ridematching-service-1.0-SNAPSHOT.jar
6. java -jar notification-service/target/notification-service-1.0-SNAPSHOT.jar
7. java -jar location-service/target/location-service-1.0-SNAPSHOT.jar
8. java -jar apigw/target/apigw-1.0-SNAPSHOT.jar

Notes: 
1. The steps should be followed as listed
2. After a successful startup. sign in through front end with details below

email: `admin@email.com`
password: `123456`

3. Use the "Signup" tab at the front end to signup "Riders only", when logged in as Admin user. You will find option to onboard drivers at the driver tab

Thats all.