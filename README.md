# TeleChatGPT Service App

## Project Overview

### Description
***I appreciate your time, so a brief presentation of the project can be found at this [link](https://youtu.be/A20ghCCXAIk)***

The TeleChatGPT Service App has been successfully implemented as a Java application leveraging the Spring Framework and Spring Security. This application seamlessly integrates a Telegram bot with Chat GPT, providing valuable services through the Telegram platform. Additionally, a robust web interface for administrators has been developed, enabling efficient management of interactions between the Telegram bot and clients.

### Technologies and Tools Used

- **Spring Boot:** The project is built using the Spring Boot framework, providing a robust and scalable backend.
- **Spring Security:** Authentication and authorization are managed using Spring Security to ensure secure access to endpoints.
- **Spring Data JPA:** Spring Data JPA simplifies database operations and enables easy interaction with the database.
- **Swagger:** Swagger is integrated to provide interactive API documentation and testing capabilities.
- **Mapstruct:** Mapstruct is used for object mapping between DTOs and entities.
- **PostgreSQL:** PostgreSQL is used as the database management system.
- **Liquibase:** Liquibase is employed for database version control and management.
- **Docker:** Docker is used for containerization of the application and database.

## Features

### 1. Telegram Bot Functionality
- Implemented a Telegram bot with functionality that effectively communicates with Chat GPT.

### 2. Web Interface for Administrators
**Registration and Login:**
- Administrators can register and log in securely, ensuring exclusive access to administrative functions.

**Chat Log Management:**
- Developed an intuitive interface for administrators to view chat logs stored in a PostgreSQL database. This feature enhances transparency and facilitates efficient monitoring.

**Client Communication:**
- Integrated a feature allowing administrators to send responses to clients directly from the web interface. These responses are seamlessly delivered to clients via Telegram.

### 3. Security Measures
- Implemented Spring Security to ensure restricted access to the web interface and guarantee secure authentication for administrators.

### 4. Website Interface
- The web interface is developed using Angular. That provides convenient operation. The interface  API interactions can also be tested easily using tools such as Postman.

## Project Completion
The project is currently prepared for deployment in docker. You can also run the project locally and watch it in action using the web interface. The source code is available on GitHub, and a comprehensive video demonstration has been provided to showcase the implemented functionality.

### To run using Docker:
***Before using the application, you need to add your own generated keys for Telegram and Open AI to properties file.***
####
- ***Docker Setup:*** Ensure that you have Docker installed on your system. You can download and install Docker from the official website: Docker Installation.
####
- ***Docker Compose:*** The application is configured to use Docker Compose for orchestrating containers. Make sure you have Docker Compose installed as well. You can check if it's installed by running:
    ```bash
    docker-compose --version
    ```
- ***Environment Variables:*** Use the ***.env*** file in `tele-chatgpt-service-api root` directory with the necessary environment variables. These variables should include your database connection details and any secret keys required by application.
 Use .env.sample file from `tele-chatgpt-service-api` root directory root directory as a sample data to connection with docker container with your custom properties.
####
- ***Build Docker Image:*** In your project root directory, open a terminal and run the following command to build a Docker image of application:
    ```bash
    docker build -t your-image-name .
    ```
####
- ***Start Docker Containers:*** Once the image is built, you can start your Docker containers using Docker Compose by running:
    ```bash
    docker-compose up
    ```
  This command will start the application and any required services (e.g., the database) defined in your docker-compose.yml file.
####
- ***Access the Application:*** After the containers are up and running, you can access your Spring Boot application.
####

## Accessing Swagger Documentation in a Web Browser
In this section, instructions are provided for accessing the Swagger documentation directly from your web browser, allowing you to explore and interact with the API endpoints easily.

### Follow these steps to access the Swagger documentation and explore API endpoints using a web browser:

- Start the Application
- Launch your preferred web browser (e.g., Chrome, Firefox, or Edge)
- In the browser's address bar, enter the URL for the Swagger documentation
    - If the application is running:
        - locally: http://localhost:8080/api/swagger-ui/index.html
        - using Docker: http://localhost:8088/api/swagger-ui/index.html

### ***Authentication and Authorization in Swagger UI:***
To gain access, use the username and password that you use to log in to the system.
If API endpoints require authorization, just make log in operation to get the authentication token to authenticate yourself.
####
### Explore Endpoints:

Once you access the Swagger UI, you will be presented with a user-friendly interface. Here, you can explore a list of available API endpoints, including their descriptions and supported HTTP methods (e.g., GET, POST, PUT, DELETE).
Swagger UI offers an interactive way to understand, test, and work with application API. Take your time to explore the available endpoints and make test requests as needed.

1. ***Select an Endpoint:***
   To get detailed information about a specific endpoint, click on it in the Swagger UI. This will expand the endpoint and display details such as request parameters, request body models (if applicable), and example responses.
####
2. ***Test an Endpoint:***
   If you want to test an endpoint interactively, click the "Try it out" button next to the endpoint. This allows you to input parameters, execute requests, and view the responses directly within Swagger UI.
####
3. ***Enjoy Your Exploring!***

## Project Contributors
I welcome contributions from the community! Whether you want to report a bug, suggest an enhancement, or submit code changes.

## Future Expansion
While there are no strict deadlines, the project is open to expansion, and additional contributors are encouraged to contribute to its continuous improvement.
