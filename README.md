# Student Task Manager

A console-based task management system built in Java, designed using clean **MVC (Model-View-Controller) architecture** with a dedicated **Service** and **Repository** layer. It allows students to create, track, update, and organize academic tasks such as assignments, projects, and deadlines — all persisted to a local text file, no database required.

This project was built as a hands-on learning exercise in backend application design: separating concerns across layers, writing testable business logic, and working with file-based persistence the same way many real systems work with a database before an ORM is introduced.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [How to Run](#how-to-run)
- [Architecture](#architecture)
- [Features](#features)
- [File Storage](#file-storage)
- [Project Structure](#project-structure)
- [Testing](#testing)

---

## Project Overview

**Student Task Manager** is a command-line application that lets a user manage a personal list of academic tasks. Each task tracks a title, subject, type (e.g. Assignment, Project, Quiz), due date, estimated hours, and completion status.

**Purpose of the project:**
- Practice designing a layered backend application (Controller → Service → Repository) without relying on a framework to do the work automatically.
- Build a real, working CRUD (Create, Read, Update, Delete) system using nothing but core Java and file I/O.
- Practice unit testing each layer independently using JUnit 5 and Mockito.
- Serve as a portfolio piece that demonstrates backend fundamentals recruiters look for: separation of concerns, testable code, and a clear data flow.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| **Java 21** | Core application language |
| **Maven** | Build tool and dependency management |
| **JUnit 5 (Jupiter)** | Unit testing framework |
| **Mockito** | Mocking dependencies in unit tests |
| **File I/O (TXT storage)** | Persistent data storage, no external database |

---

## How to Run

**1. Compile the project:**
```bash
mvn compile
```

**2. Run the application:**
```bash
java -cp target/classes Main
```

This command launches the console application. The `-cp target/classes` flag tells Java to look for compiled class files in the `target/classes` folder (created by Maven), and `Main` is the entry point class that starts the program.

Once running, you'll see a numbered menu in the terminal where you can add, view, search, update, delete, and filter tasks.

---

## Architecture

The application follows a strict **layered (MVC + Service + Repository) architecture**. Each layer has one job, and only talks to the layer directly below it. This keeps the code organized, testable, and easy to extend.

### Request Flow

```
User Input
    ↓
Controller   (handles menu choices, orchestrates the flow)
    ↓
Service      (business logic, validation, rules)
    ↓
Repository   (reads/writes the tasks.txt file)
    ↓
File System  (database/tasks.txt)
    ↓
Back up through Service → Controller → View (result shown to user)
```

### Layer Responsibilities

| Layer | Responsibility |
|---|---|
| **View** (`ConsoleView`) | Displays the menu, prints tasks to the console, and collects raw user input (title, due date, etc.). It has no business logic — it only handles input/output formatting. |
| **Controller** (`TaskController`) | Reads the user's menu choice and calls the right method. It coordinates between the View and the Service, but does not contain business rules itself. |
| **Service** (`TaskService`) | The brain of the application. Contains all business logic: preventing duplicate tasks, generating task IDs, marking tasks completed, and filtering pending vs. completed tasks. |
| **Repository** (`TaskRepository`) | Handles all direct file operations — reading from and writing to `tasks.txt`. This is the only layer that touches the file system. |
| **Model** (`Task`) | A plain data object representing a single task. It has no logic beyond storing and comparing task data. |

This separation means, for example, that if the storage method were later changed from a text file to a database, only the **Repository** layer would need to change — the Controller, Service, and View would remain untouched.

---

## Features

- **Add Task** – Create a new task with title, subject, type, due date, and estimated hours.
- **View All Tasks** – List every task currently stored.
- **Search Task** – Look up a specific task by its Task ID.
- **Update Task** – Edit the details of an existing task by ID.
- **Mark Completed** – Change a task's status from Pending to Completed.
- **Delete Task** – Remove a task permanently by ID.
- **View Pending Tasks** – Filter and display only tasks with status "Pending".
- **View Completed Tasks** – Filter and display only tasks with status "Completed".

---

## File Storage

All tasks are stored in a plain text file:

```
database/tasks.txt
```

Each line in the file represents one task, with fields separated by a pipe (`|`) character:

```
taskId|taskTitle|subject|taskType|dueDate|estimatedHours|status
```

**Example:**
```
1|Creating StudentTaskManager|Programming Java|Programming|2026-07-7|5|Completed
```

The `TaskRepository` class is solely responsible for parsing this format when reading, and rebuilding it when writing. The file is created automatically (along with its parent `database/` folder) the first time the application runs, if it doesn't already exist.

---

## Project Structure

```
StudentTaskManager/
├── database/
│   └── tasks.txt                  # Task data storage
├── src/
│   ├── main/java/
│   │   ├── Main.java              # Application entry point
│   │   ├── model/
│   │   │   └── Task.java
│   │   ├── repository/
│   │   │   └── TaskRepository.java
│   │   ├── service/
│   │   │   └── TaskService.java
│   │   ├── controller/
│   │   │   └── TaskController.java
│   │   └── view/
│   │       └── ConsoleView.java
│   └── test/java/
│       ├── model/TaskTest.java
│       ├── repository/TaskRepositoryTest.java
│       ├── service/TaskServiceTest.java
│       ├── controller/TaskControllerTest.java
│       └── view/ConsoleViewTest.java
├── pom.xml
└── README.md
```

Each folder under `src/main/java/` has its own `README.md` describing that layer in detail.

---

## Testing

The project uses **JUnit 5** (with **Mockito** for mocking dependencies) to unit test every layer independently:

- **Model tests** (`TaskTest`) – Verify the `Task` object's constructor, getters/setters, and `equals()` logic.
- **Repository tests** (`TaskRepositoryTest`) – Verify file reading/writing, updating, and deleting tasks directly against a test file.
- **Service tests** (`TaskServiceTest`) – Verify business rules (duplicate prevention, ID generation, filtering pending/completed tasks) using a **mocked** `TaskRepository`, so tests don't depend on real file I/O.
- **Controller tests** (`TaskControllerTest`) – Verify that the controller calls the correct service methods and handles menu flow correctly, using mocked `TaskService` and `ConsoleView` objects.
- **View tests** (`ConsoleViewTest`) – Verify console output formatting and input parsing.

**Run all tests with Maven:**
```bash
mvn test
```

Testing each layer in isolation (using mocks for dependencies) ensures that a bug in one layer doesn't cause unrelated tests to fail elsewhere — a standard practice in professional backend development.
