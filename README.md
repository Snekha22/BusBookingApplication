## 🚌 Online Bus Ticket Booking Application

This is a web-based application for booking bus tickets, built using Spring Boot for the backend and Thymeleaf for the frontend. It allows users to register, log in, search for bus routes, book tickets, and view their profile and booking history. The application uses MySQL for data storage and JWT for secure authentication.

---
## ✅ Features

- User Registration and Authentication: Register and log in using email and password with JWT-based security.
- Bus Search: Search for available bus routes by specifying source and destination.
- Ticket Booking: Book tickets with passenger details and receive confirmation.
- User Profile: View and update personal information and view booking history.
- Responsive UI: Built with Thymeleaf and Bootstrap for a modern, responsive design.
---
## 🚀 Technologies Used

- Backend: Spring Boot, Spring Data JPA, Spring Security, JWT
- Frontend: Thymeleaf, Bootstrap
- Database: MySQL
- Testing: JUnit, MockMvc
---
## 📑 Prerequisites

- Java 17 or later
- Maven
- MySQL
- Postman (for API testing)

## Setup Instructions

``` Clone the Repository:
git clone https://github.com/your-repo/bus-booking.git
cd bus-booking
```
---

## ⚙️ Configure MySQL:

``` Create a MySQL database named busbooking.
Update src/main/resources/application.properties with your MySQL credentials:spring.datasource.url=jdbc:mysql://localhost:3306/busbooking
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.thymeleaf.cache=false
```
---

## Build and Run the Application:
```
mvn clean install
mvn spring-boot:run
```
The application will be accessible at http://localhost:8080.

🔐 Swagger Documentation

http://localhost:8080/swagger-ui/index.html

---

## Access the Application:

Open a browser and navigate to http://localhost:8080/users/register to create a new account.
Log in at http://localhost:8080/users/login.
Search for buses at http://localhost:8080/buses/search.

---

## API Endpoints
The application exposes RESTful APIs for programmatic access. Below are the key endpoints:

POST /users/register: Register a new user
Request Body:
{
"name": "John Doe", "email": "john@example.com", "password": "password" 
}
Response: User object

POST /users/login: Authenticate a user and return a JWT token
Request Body: { "email": "john@example.com", "password": "password" }
Response: { "token": "jwt-token" }

GET /buses/search?fromLocation=CityA&toLocation=CityB: Search for buses
Response: List of available buses

POST /buses/book: Book a bus ticket
Request Body: { "busId": 1, "seatNumber": "A1", "passengerName": "Jane Doe", "passengerAge": 30, "passengerGender": "Female" }
Response: Booking confirmation

--- 
## Using Postman for API Testing

## Install Postman: Download and install Postman from https://www.postman.com/.
Test Registration:
Create a new POST request to http://localhost:8080/users/register.
Set the request body to JSON:{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password"
}
Send the request and verify the response.

## Test Login:
Create a new POST request to http://localhost:8080/users/login.
Set the request body to JSON:{
    "email": "john@example.com",
    "password": "password"
}
Copy the JWT token from the response.
Test Protected Endpoints:
For endpoints like /buses/book, add the JWT token in the Authorization header as Bearer <token>.

---
## 🧪 Running Tests
```
Run unit tests using Maven:
mvn test
```
---

## 📂 Project Structure
```
bus-booking/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── busbooking/
│   │   │   │   │   ├── config/
│   │   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   │   ├── SwaggerConfig.java
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   ├── AuthController.java
│   │   │   │   │   │   ├── BookingController.java
│   │   │   │   │   │   ├── BusSearchController.java
│   │   │   │   │   │   ├── CustomErrorController.java
│   │   │   │   │   │   ├── UserController.java
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── BookingRequestDTO.java
│   │   │   │   │   │   ├── BusSearchDTO.java
│   │   │   │   │   │   ├── ChangePasswordDTO.java
│   │   │   │   │   │   ├── PassengerDTO.java
│   │   │   │   │   │   ├── UserProfileUpdateDTO.java
│   │   │   │   │   │   ├── UserRegistrationDTO.java
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   ├── Booking.java
│   │   │   │   │   │   ├── Bus.java
│   │   │   │   │   │   ├── Passenger.java
│   │   │   │   │   │   ├── Route.java
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── BookingRepository.java
│   │   │   │   │   │   ├── BusRepository.java
│   │   │   │   │   │   ├── PassengerRepository.java
│   │   │   │   │   │   ├── RouteRepository.java
│   │   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── service/
│   │   │   │   │   │   ├── BookingService.java
│   │   │   │   │   │   ├── BusService.java
│   │   │   │   │   │   ├── RouteService.java
│   │   │   │   │   │   ├── UserDetailsServiceImpl.java
│   │   │   │   │   │   ├── UserService.java
│   │   │   │   │   ├── Application.java
│   │   │   ├── resources/
│   │   │   │   ├── static/
│   │   │   │   │   ├── css/
│   │   │   │   │   │   ├── styles.css
│   │   │   │   ├── templates/
│   │   │   │   │   ├── booking-form.html
│   │   │   │   │   ├── bus-list.html
│   │   │   │   │   ├── error.html
│   │   │   │   │   ├── history.html
│   │   │   │   │   ├── home.html
│   │   │   │   │   ├── login.html
│   │   │   │   │   ├── register.html
│   │   │   │   │   ├── profile.html
│   │   │   │   │   ├── search.html
│   │   │   │   ├── application.properties
│   ├── test/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── busbooking/
│   │   │   │   │   ├── BusBookingApplicationTest
│   ├── pom.xml
```
---
## Output
![image](https://github.com/user-attachments/assets/a43c736f-f3dd-482b-a4a6-f1f389b8b9e6)
![image](https://github.com/user-attachments/assets/2e081d83-c89c-433b-a452-a1bc7926d7f2)
![image](https://github.com/user-attachments/assets/23ca8a44-ede2-4a74-937d-31e8b8a2376b)
![image](https://github.com/user-attachments/assets/c8250321-244b-40bb-b6c3-99c73097f179)
![image](https://github.com/user-attachments/assets/fffe08a7-30ea-4810-8e5f-d045e645b83e)
![image](https://github.com/user-attachments/assets/c70df83c-1169-4e8b-953a-e280e0aa3b10)
![image](https://github.com/user-attachments/assets/303990dc-9075-44f5-986c-98ff78e8e8ad)
![image](https://github.com/user-attachments/assets/22d2856d-4269-43b4-925e-882c2808630f)
![image](https://github.com/user-attachments/assets/7f806101-17ac-4ad9-aa05-d13af6374a2b)
![image](https://github.com/user-attachments/assets/88111600-2bde-4d17-8610-48f14af611a6)












