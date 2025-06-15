# Ticket Booking Platform
## Booking Service

A Spring Boot microservice for managing bookings in the Booking Platform.

## Overview

The `booking` service handles booking creation, retrieval, and management. It is designed to run as part of a microservices-based platform, integrating with dependencies like PostgreSQL, Redis, Kafka, and ZooKeeper.

- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven

## Features

- RESTful APIs for booking operations
- Integration with PostgreSQL for persistence
- Caching with Redis
- Event publishing/consuming with Kafka

## Prerequisites

- Java 17+
- Maven 3.8+
- Docker (for containerization)
- Access to PostgreSQL, Redis, Kafka, and ZooKeeper (locally or via Kubernetes)

## Getting Started
1. **Clone the Repository**
   ```sh
   git clone
   ```
   
    cd booking-platform-infra
2. **Build the Project**
   ```sh
   mvn clean install
   ```
3. **Run the Application**
   ```sh
    mvn spring-boot:run
    ```
4. **Access the Service**
5. Open your browser and navigate to `http://localhost:8080/api/bookings` to access the booking APIs.
6. **Dockerize the Application**
   ```sh
   docker build -t booking-service .
   docker run -p 8080:8080 booking-service
   ```
7. **Deploy to Kubernetes**
8. Apply the Kubernetes manifests:
   ```sh
   kubectl apply -f k8s/booking-service/deployment.yaml
   kubectl apply -f k8s/booking-service/service.yaml
   ```

