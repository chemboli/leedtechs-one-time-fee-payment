# 🎓 LeedTech One-Time Fee Payment System

## 📌 Overview

This project is a full-stack application designed to manage student one-time fee payments. It allows administrators to create student accounts, track balances, and process payments while ensuring that overpayments are not allowed.

---

## ⚙️ Tech Stack

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Maven

### Frontend

* Angular
* TypeScript

### Database

* H2 Database (in-memory)

---

## 🚀 Features

* Create and manage student accounts
* Retrieve student details
* Process one-time payments
* Prevent overpayment beyond required fee
* Automatically update and track balances
* Unit testing for service layer

---

## 📂 Project Structure

```
leedtech-student-fee-payment
├── Backend   # Spring Boot application
├── Frontend  # Angular application
└── README.md
```

---

## 🛠️ Setup Instructions

### 🔹 Prerequisites

Make sure you have the following installed:

* Java 17+
* Maven
* Node.js & npm
* Angular CLI
* H2 Database (in-memory)

---

### 🔹 Backend Setup

1. Navigate to the backend folder:

   ```bash
   cd backend
   cd fee-payment
   ```

2. The application uses an in-memory H2 database by default, so no external database setup is required.

   You can access the H2 console at:

   ```
   http://localhost:8080/h2-console
   ```

   Example configuration (if needed):

   ```properties
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.h2.console.enabled=true
   ```

3. Build and run the application:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. The backend will start on:

   ```
   http://localhost:8080
   ```

---

### 🔹 Frontend Setup

1. Navigate to the frontend folder:

   ```bash
   cd Frontend
   cd leedtechs-fee-payment
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Run the Angular app:

   ```bash
   ng serve
   ```

4. Open in browser:

   ```
   http://localhost:4200
   ```

---

## 🔌 API Endpoints (Sample)

| Method | Endpoint       | Description         |
| ------ | -------------- | ------------------- |
| POST   | /students      | Create a student    |
| GET    | /students/{id} | Get student details |
| POST   | /payments      | Make a payment      |

---

## 🧪 Running Tests

To run backend tests:

```bash
mvn test
```

---

## ⚠️ Assumptions

* Each student has a fixed total fee
* Payments cannot exceed the remaining balance
* System handles basic validation and error handling

---

## ✨ Improvements (Future Work)

* Authentication and authorization (Admin roles)
* Payment history tracking
* UI enhancements with Angular Material
* Deployment (Docker / Cloud)

---

## 📎 Repository Link

[https://github.com/chemboli/leedtechs-one-time-fee-payment](https://github.com/chemboli/leedtechs-one-time-fee-payment)

---

## 👤 Author

**Chemboli Roywinfeel Senase Yoh**

---

## 📧 Notes

This project was completed as part of a technical assessment for a Fullstack Developer position. It demonstrates backend logic, frontend integration, and testing practices.

<img width="952" height="478" alt="image" src="https://github.com/user-attachments/assets/d56e84c5-52e5-4639-adf3-699b7e3deb6b" />

