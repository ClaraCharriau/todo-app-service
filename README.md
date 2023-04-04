
# Todo App API

A Java / Gradle / Spring Boot (v.3.0.4) / REST API service that can be connected to the todo app Angular project.

## About the service

A simple Todolist application REST service, that connect with a PostgreSQL database.

### What I learned from this project

- Discovery of Spring Framework : inversion of control, dependency injection, annotations...
- Writing a RESTful service using annotations, supporting JSON requests and responses
- Personnalized exceptions
- Spring Data integration
- Entities to Dto mapping
- Unit testing with JUnit 5

### Get all Todos
```json
GET /todolist
Accept: application/json
Content-Type: application/json

{
    "id": "0464c4ee-9f34-492a-bc6f-f105ca841c30",
    "content": "responds to urgent emails",
    "category": "work",
    "urgent": true,
    "doneDate": "2023-03-24T10:39:28.977Z"
}

RESPONSE: HTTP 200
```

### Get a specific todo
```json
GET /todolist/{id}
Accept: application/json
Content-Type: application/json

{
    "id": "0464c4ee-9f34-492a-bc6f-f105ca841c30",
    "content": "responds to urgent emails",
    "category": "work",
    "urgent": true,
    "doneDate": "2023-03-24T10:39:28.977Z"
}

RESPONSE: HTTP 200
```

### Create a todo

```json
POST /todolist
Accept: application/json
Content-Type: application/json

{
    "id": "0464c4ee-9f34-492a-bc6f-f105ca841c30",
    "content": "responds to urgent emails",
    "category": "work",
    "urgent": true,
    "doneDate": "2023-03-24T10:39:28.977Z"
}

RESPONSE: HTTP 201 (Created)
```
