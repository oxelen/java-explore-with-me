# Explore With Me

Backend service for publishing and discovering events.  
Users can create events, explore public events, and send participation requests.

The application is built using a microservice architecture and consists of a main service and a statistics service.

## Features

- Create and manage events
- Browse and search public events
- Send participation requests
- Event moderation by administrators
- Event statistics (views tracking)
- Pagination and filtering
- Role-based access (public / user / admin)

## Architecture

The application consists of several services:

- **Main Service** – manages users, events, requests, and categories
- **Stats Service** – collects and provides event view statistics
- **PostgreSQL databases** for each service

All services run in separate Docker containers.

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Lombok
- Docker
- REST API
- JUnit / Mockito
- Postman

## API

The project exposes REST endpoints for three types of access:

### Public API

- View published events
- Search events by filters
- View categories
- View event statistics

### User API

- Create events
- Update events
- Send participation requests
- Manage own events

### Admin API

- Moderate events
- Manage categories
- Manage users
- View statistics

## Running the Project

### 1. Clone the repository

```bash
git clone https://github.com/oxelen/java-explore-with-me.git
```

### 2. Run with Docker

```bash
docker-compose up
```

### 3. API will be available at

```
http://localhost:8080
```

## Project Structure

```
main-service
 ├─ controller
 ├─ service
 ├─ repository
 ├─ model
 ├─ dto
 ├─ mapper
 └─ exception

stats-service
 ├─ controller
 ├─ service
 ├─ repository
 └─ model
```

## Testing

Tests are implemented using:

- JUnit
- Mockito

API testing was performed with Postman.
