# IU Forum

## 1. Introduction

**IU Forum** is a web-based discussion forum designed for students. The system allows users to register accounts, log in, create posts, comment on posts, like posts, bookmark posts, search for content, and report inappropriate content. It also includes an admin dashboard for managing users, posts, categories, reports, and user roles.

The project is built with **Spring Boot**, **Thymeleaf**, **Spring Security**, **Spring Data JPA**, and **MySQL**.

---

## 2. Main Features

### User Features

- Register a new account.
- Verify account using OTP via email.
- Log in and log out.
- View posts on the homepage.
- Create, edit, and delete personal posts.
- View post details.
- Comment on posts.
- Like posts.
- Save posts as bookmarks.
- Search for posts.
- View posts by category.
- Update personal profile information.
- Change password.
- Report inappropriate posts or comments.
- View notifications.

### Admin Features

- View the admin dashboard.
- Manage users.
- Lock or unlock user accounts.
- Assign or update user roles.
- Manage posts.
- Pin or lock posts.
- Manage categories.
- View and handle reports.
- Manage role definitions.

---

## 3. Technologies Used

- Java 17
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Maven
- HTML, CSS, JavaScript

---

## 4. Requirements

Before running the project, make sure your computer has:

- JDK 17 or later
- MySQL Server
- Maven
- An IDE such as IntelliJ IDEA, Eclipse, or VS Code

---

## 5. Database Setup

Open MySQL and run the following commands to create the database:

```sql
CREATE DATABASE IF NOT EXISTS forum_db;
USE forum_db;
```

The database configuration is located in:

```text
src/main/resources/application.properties
```

Check and update the MySQL username and password to match your local machine:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forum_db
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

Note: The project uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Therefore, when the project runs for the first time, Hibernate will automatically create the required tables in the database.

---

## 6. Email OTP Configuration

The project supports OTP verification through Gmail SMTP. In the file:

```text
src/main/resources/application.properties
```

update the email configuration:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Note: `spring.mail.password` should be a **Google App Password**, not your normal Gmail password.

---

## 7. How to Run the Project

### Option 1: Run with Maven

Open a terminal in the project folder and run:

```bash
mvn spring-boot:run
```

### Option 2: Run with an IDE

Open the project in your IDE and run the main application file:

```text
src/main/java/com/example/forum/ForumApplication.java
```

After the project starts successfully, open your browser and visit:

```text
http://localhost:8080
```

---

## 8. Demo Accounts

When the project runs for the first time, sample data will be created automatically by `DataInitializer`.

### Admin Account

```text
Username: admin
Password: admin123
```

### User Account

```text
Username: student
Password: student123
```

---

## 9. Quick User Guide

### For Users

1. Go to `http://localhost:8080`.
2. Register a new account or log in using a demo account.
3. View posts on the homepage.
4. Click **Create Post** to create a new post.
5. Open a post detail page to comment, like, bookmark, or report the post.
6. Go to the profile page to update personal information or change the password.
7. Use the search bar to find posts.

### For Admin

1. Log in with the admin account.
2. Go to the admin dashboard:

```text
http://localhost:8080/admin
```

3. Manage users, posts, categories, reports, and roles from the admin pages.

---

## 10. Main Project Structure

```text
forum
├── src
│   ├── main
│   │   ├── java/com/example/forum
│   │   │   ├── config
│   │   │   ├── controller
│   │   │   ├── dto
│   │   │   ├── model
│   │   │   ├── repository
│   │   │   └── service
│   │   └── resources
│   │       ├── static
│   │       │   ├── css
│   │       │   └── js
│   │       ├── templates
│   │       └── application.properties
│   └── test
├── pom.xml
└── README.md
```

---

## 11. Notes Before Uploading to GitHub

Do not publish sensitive information such as:

- Real MySQL passwords
- Gmail App Passwords
- Configuration files containing private information

Before committing the project to GitHub, replace sensitive values in `application.properties` with sample values.
