# Events API

API for **event management**, with **login/password authentication** and **PostgreSQL** as the database.

## Authentication

The API is protected.  
Before accessing any business endpoint, you must create a user and log in.

### Base URL
/auth

### Endpoints

| Method | Endpoint | Description |
|------|---------|-------------|
| POST | /auth/register | Creates a new user |
| POST | /auth/login | Authenticates the user |

After login, use the returned token to access protected endpoints.

## Event Management

Event management is handled through the following base URL:

/api/event

All endpoints under this route **require authentication**.

This API is responsible for:
- Creating events  
- Listing and retrieving events  
- Updating events  
- Removing or disabling events  

## Access Rules

- Requests without authentication are blocked
- A user must be created via `/auth/register` before consuming the API
- Only authenticated users can access `/api/event`

## Technologies

- Java (Spring Boot)
- PostgreSQL
- Maven
- JPA / Hibernate

## Database Configuration

Database access is configured using environment variables in `application.properties`:

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/database_name
DATABASE_USER=username
DATABASE_PWD=password
```

spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PWD}

Running the API:

git clone https://github.com/your-username/your-repository.git
cd your-repository
mvn spring-boot:run

Requirements:

PostgreSQL running
Database created

License:
MIT
