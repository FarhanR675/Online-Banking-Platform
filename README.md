# OnlineBankingPlatform

## Overview
OnlineBankingPlatform is a web-based banking application designed to provide users with secure and straightforward banking operations. The application includes functionalities such as user registration, login, deposits, and withdrawals.

## Features
- **User Registration & Login:** Secure user authentication and authorization.
- **Deposit & Withdrawal:** Basic banking functionalities for managing user accounts.
- **Account Overview:** View current account balance.
- **Responsive Design:** User-friendly interface built with HTML, CSS, and JavaScript.

## Tech Stack
- **Backend:** Java, Spring Boot
- **Frontend:** HTML, CSS, JavaScript
- **Database:** MongoDB
- **Build Tool:** Maven

## Installation

### Prerequisites
- Java 22 or higher
- Maven 3.6 or higher
- MongoDB 5.x or higher

### Steps to Run Locally

1. **Clone the repository:**
git clone https://github.com/FarhanR675/Online-Banking-Platform.git
   
Navigate to the project directory:

cd Online-Banking-Platform

Install dependencies and build the project:

mvn clean install

Set up MongoDB:

    Ensure MongoDB is running on localhost:27017.
    *IMPORTANT*
    In MongoDB, ensure to create a Database named "bank" and a Collection named "currentAccounts".
    (Optional) Modify the application.properties to match your MongoDB setup.

Run the application:

    mvn spring-boot:run

    Access the application:
        Open your browser and navigate to http://localhost:8080/register.html.
        *NOTE*
        Once you registered, you will be given your Bank ID and Bank Pin, which will be
        required to login

Usage

    Register a new user.
    Log in with your credentials.
    Perform banking operations such as deposit and withdrawal.
    View your account balance.


This project is licensed under the MIT License - see the LICENSE file for details.
Contact

For any inquiries, please reach out to FarhanRahman675@gmail.com.

