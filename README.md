# Auth Service

This is a basic authentication service providing user registration, authentication (login), and authorization checks. The service is built using Spring Boot and is intended to be integrated as a backend module in larger systems.

## Endpoints

All endpoints are prefixed with `/api`.

### `POST /api/register`

**Registers a new user.**

- **Request Body**:
  ```json
  {
    "username": "imusername",
    "password": "supersecret123"
  }
- **Response (201 CREATED)**
  ```json
  {
    "id": "a435bf57-f4b8-6j3h-g362-a5adbffd6fft",
    "username": "imusername",
    "token": "eyJhbGciOiJIUzI1NiJ4.eyJzdWIiOiJkYm3ocnkiLCJleHAiOjE3NDcwNTA4NTcsImlhdCI6MTc0Njg3ODE1NywiYXV0aG9yaXRpZXMiOlsiQURNSU4iLCJVU0VSIl19.PRcIp7Jds65ScSSxBgzLFImP4BF5LcAHYAx7Q1y7ij5",
    "expirationDate": "2025-05-12T11:55:57.349Z",
    "roles": [
        "USER"
    ]
}

### `POST /api/authenticate`

**Authenticates an existing user and returns a JWT token.**

- **Request Body**:
  ```json
  {
    "username": "imusername",
    "password": "supersecret123"
  }
- **Response (200 OK)**
  ```json
  {
    "id": "a435bf57-f4b8-6j3h-g362-a5adbffd6fft",
    "username": "imusername",
    "token": "eyJhbGciOiJIUzI1NiJ4.eyJzdWIiOiJkYm3ocnkiLCJleHAiOjE3NDcwNTA4NTcsImlhdCI6MTc0Njg3ODE1NywiYXV0aG9yaXRpZXMiOlsiQURNSU4iLCJVU0VSIl19.PRcIp7Jds65ScSSxBgzLFImP4BF5LcAHYAx7Q1y7ij5",
    "expirationDate": "2025-05-12T11:55:57.349Z",
    "roles": [
        "USER"
    ]
}

### `POST /api/authorize`

**Validates whether the currently authenticated user has the required authority.**

Headers:

    Authorization: Bearer <JWT token>

Response:

    200 OK if authorized

    403 FORBIDDEN if not authorized