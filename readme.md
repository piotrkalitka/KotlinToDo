# ToDo Application - Kotlin + Spring Boot 2

## Project Description
A simple task management (ToDo) application written in Kotlin, using Spring Boot 2 as the backend. The application supports basic CRUD operations (Create, Read, Update, Delete) and has an authentication system based on JWT.

## Features
- User registration
- User login using JWT
- Adding new tasks
- Reading task list
- Editing existing tasks
- Deleting tasks

## Technologies
- Kotlin
- Spring Boot 2
- Spring Security
- JWT (JSON Web Token)
- Hibernate
- PostgreSQL
- Liquibase
- Maven / Gradle

## Running the Application
1. Clone the repository:
   ```bash
   git clone https://github.com/piotrkalitka/KotlinToDo.git
   ```
2. Navigate to the project directory:
   ```bash
   cd todo-app
   ```
3. Build and run the project:
   ```bash
   docker-compose up
   ```

## API Endpoints
### Authentication
- `POST /api/auth/register` – user registration
- `POST /api/auth/login` – login, returns JWT

### Tasks
- `GET /api/todos` – retrieve task list
- `POST /api/todos` – add a new task
- `PUT /api/todos/{id}` – update a task
- `DELETE /api/todos/{id}` – delete a task

## Example Login Request
```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"user", "password":"password"}'
```

## Example Task Creation Request
```bash
curl -X POST http://localhost:8080/api/todos \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <JWT_TOKEN>" \
-d '{"title":"New Task", "description":"Task description"}'
```

## Next Steps
- Implementation of unit and integration (Spock) tests
- Integration of Redis
- Integration of Kafka or RabbitMQ depending on requirements
- Separation of a user management microservice

## Author
[Piotr Kalitka / piotrkalitka]

## License
MIT License

