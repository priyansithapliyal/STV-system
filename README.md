# STV Election System

## 📌 Project Overview

The STV Election System is a JavaFX-based desktop application developed to simulate a secure and interactive election process using the Single Transferable Vote (STV) algorithm. The system allows administrators to manage candidates and enables users to cast ranked ballots. The application also stores election data permanently using MySQL and JDBC connectivity.

This project demonstrates frontend-backend integration, database connectivity, GUI development, and implementation of election algorithms.

---

# 🚀 Features

## 👑 Admin Features

* Secure admin login
* Add and manage candidates
* View election results
* Reset election data
* Monitor vote counts dynamically

## 👤 User Features

* Secure voter login
* Cast ranked ballots (1st, 2nd, 3rd preference)
* One user = one vote system
* View election results after voting

## 📊 Election Features

* STV (Single Transferable Vote) implementation
* Vote counting and candidate elimination
* Winner highlighting
* Dynamic result display using TableView
* MySQL database integration for persistent storage

---

# 🛠 Technologies Used

* Java
* JavaFX
* FXML
* CSS
* JDBC
* MySQL
* Eclipse IDE

---

# 📂 Project Structure

## controller

Handles UI interactions and application flow.

Files:

* LoginController.java
* DashboardController.java
* CandidateController.java
* BallotController.java
* ResultController.java
* UserDashboardController.java

## model

Contains data classes and entities.

Files:

* Candidate.java
* Ballot.java

## service

Contains backend business logic and STV implementation.

Files:

* ElectionService.java

## util

Contains utility/helper classes.

Files:

* SceneSwitcher.java

## database

Handles database connectivity.

Files:

* DatabaseConnection.java

## session

Stores current logged-in user information.

Files:

* UserSession.java

## view

Contains all JavaFX FXML UI files.

Files:

* login.fxml
* dashboard.fxml
* userDashboard.fxml
* candidate.fxml
* ballot.fxml
* result.fxml

---

# 🗄 Database Integration

The system uses MySQL database with JDBC connectivity.

### Tables Used

* users
* candidates
* ballots

### Database Features

* Persistent candidate storage
* Persistent ballot storage
* User authentication
* Vote tracking using has_voted field

---

# 🔐 Authentication System

The project implements role-based login:

### Admin

* Access to admin dashboard
* Candidate management
* Result management

### Voter

* Access to user dashboard
* Voting access
* Result viewing
* Duplicate voting prevention

---

# 🧠 OOP Concepts Used

## Encapsulation

Used in model classes like Candidate and Ballot using private fields with getters/setters.

## Inheritance

JavaFX classes inherit functionality from parent UI classes.

## Abstraction

FXML controllers abstract UI logic from backend processing.

## Polymorphism

Method overriding used in JavaFX TableRow customization and event handling.

---

# 📈 Future Improvements

* Graphical result charts
* Export results as PDF
* Online voting support
* Email verification
* Search and filter candidates

---

# ▶ How to Run the Project

1. Clone the repository
2. Import project into Eclipse
3. Configure JavaFX library
4. Configure MySQL database
5. Run Main.java

---

# 👨‍💻 Team Members

* Aman Negi – Frontend Development
* Priyanshu – UI Styling and Navigation
* Vanshika Saini – Backend Logic and Controllers  with Database Integration
* Priyanshi – Backend Logic and controllers

---

# 📌 Conclusion

The STV Election System successfully demonstrates JavaFX GUI development, JDBC database connectivity, backend logic implementation, and the working of the Single Transferable Vote election algorithm in a desktop-based environment.
