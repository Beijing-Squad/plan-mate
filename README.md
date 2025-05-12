# Plan Mate ✨

<div align="center">

![PlanMate Logo](https://img.shields.io/badge/PlanMate-Task%20Management-brightgreen?style=for-the-badge&logo=kotlin)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-blue.svg)](https://kotlinlang.org)
[![MongoDB](https://img.shields.io/badge/MongoDB-4.10.1-green.svg)](https://www.mongodb.com/)
[![TDD](https://img.shields.io/badge/TDD-100%25%20Coverage-orange.svg)](https://en.wikipedia.org/wiki/Test-driven_development)
[![SOLID](https://img.shields.io/badge/SOLID-Principles-red.svg)](https://en.wikipedia.org/wiki/SOLID)

<p align="center">
  <b>A robust CLI task management application built with Kotlin, following TDD approach and SOLID principles</b>
</p>

</div>

<hr>

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Technologies](#-technologies)
- [Installation](#-installation)
- [Usage](#-usage)
- [User Types](#-user-types)
- [Testing](#-testing)
- [Data Migration](#-data-migration)

<hr>

## 🚀 Overview

PlanMate is a powerful command-line task management application designed to streamline project workflows. Built with Kotlin and following Test-Driven Development (TDD) methodology, PlanMate enables teams to efficiently manage tasks across multiple projects with different workflow states.

## ✨ Features

- **User Authentication**: Secure login with MD5 password hashing
- **Multi-Project Support**: Organize tasks across different projects
- **Dynamic Task States**: Customizable workflow states (e.g., TODO, In Progress, Done)
- **Role-Based Access Control**: Admin and Mate user types with different permissions
- **Task Management**: Create, edit, delete, and view tasks
- **Project Management**: Admin-controlled project creation and configuration
- **Swimlane Visualization**: Console-based Kanban board view
- **Comprehensive Audit System**: Track all changes to projects and tasks
- **Cloud Data Storage**: MongoDB integration for seamless data persistence
</div>

- **Domain Layer**: Contains business logic, entities, and repository interfaces
- **Data Layer**: Implements repository interfaces, handles data persistence
- **UI Layer**: Handles user interaction through CLI

## 💻 Technologies

- **Kotlin**: Primary programming language
- **MongoDB**: Cloud database for data persistence
- **Koin**: Dependency injection framework
- **Coroutines**: Asynchronous programming
- **JUnit 5**: Testing framework
- **Mockk**: Mocking library for testing

## 🔧 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Beijing-Squad/plan-mate.git
   cd plan-mate
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew run
   ```

## 📝 Usage

### Basic Commands

```bash
# Login to the system
login --username admin --password admin123

# Create a new project (Admin only)
create-project --name "Website Redesign" --description "Redesign company website"

# Add a new task state to a project (Admin only)
add-state --project-id WEB-01 --state "Code Review"

# Create a new task
create-task --project-id WEB-01 --title "Design Homepage" --description "Create initial mockups" --state "TODO"

# View tasks in swimlane format
view-swimlanes --project-id WEB-01

# View audit history
view-audit --project-id WEB-01
view-audit --task-id WEB-01-002
```

## 👥 User Types

PlanMate supports two user roles with different permissions:

### Admin
- Create and manage projects
- Define and modify task states
- Create, edit, and delete tasks
- Create mate users
- Access audit history
- View all projects and tasks

### Mate
- Create, edit, and delete tasks in assigned projects
- Update task states
- View assigned projects and tasks
- Access audit history for their tasks

## 🧪 Testing

PlanMate follows Test-Driven Development methodology with 100% test coverage:

```bash
# Run all tests
./gradlew test

# Generate test coverage report
./gradlew jacocoTestReport
```

Test Structure:
- Unit tests for domain logic
- Integration tests for repository implementations
- End-to-end tests for CLI commands

## 🔄 Data Migration

PlanMate has undergone a data source migration from CSV files to MongoDB:

- **Before**: Data stored in CSV files
- **After**: Data stored in MongoDB cloud database

The migration leveraged the Dependency Inversion Principle, allowing seamless transition with minimal changes to domain and UI layers.

### Key Migration Changes

- Implemented MongoDB repositories
- Added coroutine support for asynchronous database operations
- Updated repository interfaces to support suspending functions
- Maintained backward compatibility with existing features
