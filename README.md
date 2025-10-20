# 🏨 API Testing Framework – Hotel Booking Website

This project is a **Java-based API testing framework** built using **Cucumber** and **RestAssured**.
It automates testing of RESTful APIs for a **Hotel Booking website**, covering end-to-end scenarios such as authentication, booking creation, retrieval, update, and deletion.

---

## 1. 🧾 Overview

This framework follows the **Behavior-Driven Development (BDD)** approach, where test scenarios are written in plain English (Gherkin syntax) to improve collaboration between technical and non-technical team members.

It uses **Cucumber** to define test behavior, **RestAssured** for API interactions, and **JUnit** as the test execution framework.
Each component of the framework is modularized to promote scalability, readability, and reusability.

* Base URL: `https://automationintesting.online/`
* Swagger Docs:

    * Auth: `https://automationintesting.online/auth/swagger-ui/index.html`
    * Booking: `https://automationintesting.online/booking/swagger-ui/index.html`

---

## 2. 🧩 Tech Stack

| Category                 | Technology / Pattern                                    | Description                                                   |
|--------------------------|---------------------------------------------------------|---------------------------------------------------------------|
| **Language**             | Java 17                                                 | Primary programming language                                  |
| **Build Tool**           | Maven 4.0.0                                             | Dependency and build management                               |
| **Frameworks**           | Cucumber 7.22.2, RestAssured 5.5.6                      | API automation and BDD implementation                         |
| **Testing Library**      | JUnit Jupiter / Platform Suite                          | Test execution and orchestration                              |
| **JSON Handling**        | JSON Schema Validator                                   | Data serialization and schema validation                      |
| **Annotations Utility**  | Lombok                                                  | Reduces boilerplate code                                      |
| **Development Approach** | **BDD Pattern**                                         | Help non technical members to participate in automating tests |
| **Design Patterns**      | **Context Object Pattern**, **Factory/Builder Pattern** | Promotes reusability, readability, and maintainability        |
| **Reporting**            | Cucumber HTML & JSON Reports                            | Test reporting and visualization                              |

---

## 3. 📁 Project Structure

```
API_Testing_kata/
│
├── pom.xml                         # Maven dependencies and build configuration
├── src/
│   └── test/
│       ├── java/com/booking/
│       │   ├── TestRunner.java                # Cucumber test runner
│       │   ├── context/                       # Manages shared test context
│       │   ├── hooks/                         # Setup & teardown logic
│       │   ├── pojo/                          # Data model classes
│       │   ├── routes/                        # API endpoint definitions
│       │   ├── stepDefinitions/               # Step definitions for feature files
│       │   ├── testData/                      # Dynamic payload generation
│       │   └── utils/                         # Utility/ Helper methods (logging, reading/writing config files, validating inputs, and processing test data etc.)
│       │
│       └── resources/
│           ├── features/                      # Cucumber feature files
│           │   ├── 01_authentication.feature
│           │   ├── 02_createBooking.feature
│           │   ├── 03_getBooking.feature
│           │   ├── 04_updateBooking.feature
│           │   └── 05_deleteBooking.feature
│           │
│           ├── config/                        # Config files and JSON schemas
│           │   ├── config.properties
│           │   ├── authenticationResponseSchema.json
│           │   ├── createBookingResponseSchema.json
│           │   └── deleteResponseSchema.json
│           │
│           └── spec/                          # OpenAPI or Postman spec
│               └── booking.yaml
│
└── target/
    ├── cucumber-reports.html                  # Generated HTML report
    └── cucumber.json                          # JSON report output
```

---

## 4. ⚙️ Setup Instructions

### 4.1 Prerequisites

Ensure the following are installed on your system:

* **Java JDK 17**
* **Maven 4+**
* **An IDE** (e.g., IntelliJ IDEA or Eclipse)

### 4.2 Clone the Repository

```bash
git clone https://github.com/lakshmiramagovindan/Kata_API.git
cd Kata_API
```

### 4.3 Install Dependencies

```bash
mvn clean install
```
## 5. ▶️ Running Tests

You can execute the Cucumber tests using Maven commands or directly from your IDE.

---

### 🧩 Option 1: Using Maven

Run all Cucumber feature files through the configured Test Runner.

#### Run tests and build the project:

```bash
mvn clean install
```

This command cleans previous builds, compiles the project, and automatically executes all Cucumber feature files via the **TestRunner.java** class.

#### Run tests only:

```bash
mvn test
```

This command skips the build phase and runs the Cucumber tests directly. Reports are generated in the `target/` directory after execution.

---

### 🧩 Option 2: Using the IDE (Cucumber CLI)

You can run the `TestRunner.java` file directly from your IDE:

```
src/test/java/com/booking/TestRunner.java
```

This executes all feature files from the path specified in the runner’s `@ConfigurationParameter`.

---

### 🧩 Option 3: Running Individual Feature Files (from IDE)

To execute specific scenarios or feature files:

```
src/test/resources/features/*.feature
```
Open a feature file and click the Run button in your IDE to execute it directly using the built-in Cucumber or JUnit runner.

---

✅ **Tip:** Use tags (e.g., `@postive`, `@negative`, `@createBooking`) in your feature files and specify them in the `@ConfigurationParameter` to control which scenarios run.

## 6. 🏷️ Available Tags

Tags are used to group and selectively run specific test scenarios.

| Tag                 | Description                       |
| ------------------- | --------------------------------- |
| **@positive**       | Happy path scenarios              |
| **@negative**       | Error or negative test cases      |
| **@authentication** | Authentication-related tests      |
| **@createBooking**  | Booking creation tests            |
| **@getBooking**     | Booking retrieval tests           |
| **@updateBooking**  | Booking update tests              |
| **@deleteBooking**  | Booking deletion tests            |
| **@e2e**            | End-to-end booking flow scenarios |

✅ **Tip:** You can execute specific tags from the command line using:

```bash
mvn test -Dcucumber.filter.tags="@e2e"
```


## 7. 📊 Reports

After execution, reports are generated in the `target/` directory:

* **HTML Report:** `target/cucumber-reports.html`
* **JSON Report:** `target/cucumber.json`

Open the HTML report in any browser for detailed scenario results.

---

## 8. 🧠 Key Features

* Modular and maintainable BDD structure
* Data-driven testing using POJOs
* JSON schema validation for response structure
* Context-based data sharing between steps
* Centralized route and endpoint management
* Reusable test utilities and payload builders

---

## 9. 🧪 Test Scenarios

## Test Scenarios Overview

### Authentication
- **Authenticate User (Positive)** – Obtain token using valid credentials.
- **Authenticate User (Negative)** – Attempt authentication with invalid or missing credentials and verify error handling.

### Booking Creation
- **Create Booking (Positive)** – Validate successful booking creation response.
- **Create Booking (Negative)** – Test booking creation with invalid or incomplete payload and verify API rejects it appropriately.
- **Create Booking (Negative)** – Test booking creation with invalid request specifications and verify API rejects it appropriately.

### Retrieve Booking
- **Get Booking (Positive)** – Fetch booking details by valid ID.
- **Get Booking (Negative)** – Attempt fetching booking with invalid or non-existent ID and verify proper error response.

### Update Booking
- **Update Booking (Positive)** – Modify an existing booking with valid data and confirm update success.
- **Update Booking (Negative)** – Attempt update with invalid payload and verify error handling.
- **Update Booking (Negative)** – Attempt update with invalid request specification or on a non-existent booking and verify error handling.

### Delete Booking
- **Delete Booking (Positive)** – Remove an existing booking and verify successful deletion.
- **Delete Booking (Negative)** – Attempt deletion of a non-existent booking or without authorization and verify appropriate error response.
- **Delete Booking (Negative)** – Attempt retrieval of a deleted booking and verify appropriate error response.


---

## 10. 🔍 Observations

All observations (open issues) with respect to various test scenarios, swaggers and booking websites are listed below.     
Also, please note that some tests have comments referencing these issues and may fail intentionally to highlight inconsistencies or bugs.

1. **Swagger Documentation** – The Swagger documentation URL returns a **404 Not Found** error.

2. **PATCH Request Handling** – The API does not support **PATCH** requests for the `/booking/{id}` endpoint and returns a **“Method Not Allowed”** error.

3. **Health Check & Authentication Endpoints** – When accessed through Swagger, these endpoints do not return appropriate status codes or responses.

4. **Authentication Endpoint Validation** – The `username` and `password` fields are mandatory. However, when omitted, the API returns a **401 Unauthorized** status instead of the expected **400 Bad Request**, indicating improper configuration for handling missing required fields.

5. **Response Schema Mismatch** – The **Create** and **Get Booking** APIs do not include `email` and `phone` fields in their responses, even though both are defined in the **Booking.yaml** and Swagger specifications.

6. **Lastname Field Validation** – In both **Create** and **Update Booking** APIs, validation for the `lastname` field is incorrect. The error message states **“size must be between 3 and 30”**, while the expected constraint (as per Booking.yaml) is **“size must be between 3 and 18”**.

7. **Inconsistent Error Message Format** – For missing mandatory fields in **Create** and **Update Booking** APIs, the error responses are inconsistent. Some fields return messages like **“<fieldName> must not be blank”**, while others simply return **“Failed to create booking”**, indicating a lack of standardized error formatting.

8. **Incorrect Status Codes for Date Validations** –

    * When `checkin date > checkout date`, the API returns **409 Conflict** instead of the expected **400 Bad Request**.
    * When `checkin date = checkout date`, the API also returns **409 Conflict**, but per the Booking.yaml specification, it should return either **201 Created** or **400 Bad Request**, depending on whether same-day checkout is permitted.

9. **Invalid Token Handling** – In **Get** and **Update Booking** APIs, random status codes are returned when a valid token is not passed in cookies. As per API standards, it should consistently return **403 Forbidden**.

10. **Delete Booking API Error Handling** – When booking ID, authentication token, or cookies are missing or invalid, the API responds with **500 Internal Server Error** instead of an appropriate **4xx** client error status code (e.g., 400, 401, or 403).

11. **Random 5XX Series Errors** –
    The server occasionally responds with random 5XX series errors (e.g., 500 Internal Server Error, 502 Bad Gateway, 503 Service Unavailable) even when the request is valid and well-formed.
---

**Author:** R G Lakshmi  
**Version:** 1.0.0
