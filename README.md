# REST Bookstore Application

## Overview

The **Syebookstore** is a full-featured REST API that allows users to manage accounts, browse and manage book information, upload and download books in PDF format, and manage user reviews. The system uses a robust, scalable backend powered by Spring Boot and integrates with a PostgreSQL database for persistence.

## Features

- **User Account Management**: Create, update, and manage user accounts with secure authentication and authorization.
- **Book Catalog**: Add, update, and delete books in the catalog with full information such as title, author, and description.
- **PDF Upload & Download**: Upload and download books as PDF files for easy access.
- **Review Management**: Post and manage book reviews, linked to user accounts and books.
- **Authentication & Authorization**: Custom handlers ensure secure access control to all resources.
- **Database Versioning**: Managed via Liquibase to handle schema changes smoothly.

## Tech Stack

- **Framework**: Spring Boot
- **Persistence**: Spring Data JPA, Hibernate
- **Database**: PostgreSQL with Liquibase for database migrations
- **Logging**: SLF4J
- **Testing**: JUnit, Mockito, Testcontainers
- **Containerization**: Docker

## Architecture

The application follows the **RESTful** architectural style where clients communicate with the server via **HTTP** requests and responses. Authentication and authorization are handled through custom-built handlers for **fine-grained control** over resource access.

### Key Components:
1. **Spring Boot**: Handles the core application logic, including request routing and business logic.
2. **Spring Data JPA**: Simplifies database interaction, using **Hibernate** as the ORM for mapping objects to PostgreSQL.
3. **PostgreSQL**: Ensures reliable storage for books, users and reviews.
4. **Liquibase**: Ensures that the database schema is managed in a version-controlled manner.
5. **Testcontainers**: Allows integration testing by spinning up containerized PostgreSQL databases for tests.
6. **Custom Authentication & Authorization**: Ensures that users can only access the data and resources they’re authorized for.

### Additional Details

API details can be found on [Wiki](https://github.com/syegod/syebookstore/wiki).
