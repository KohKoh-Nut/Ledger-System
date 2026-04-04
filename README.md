# CS2030S Practical Revision: Functional Ledger System

A Java-based implementation of a financial tracking system developed for **CS2030S** practical exam revision. This project explores the intersection of **Object-Oriented Programming (OOP)** and **Functional Programming (FP)**, focusing on immutability, type safety, and the Streams API.

## Key Concepts Explored

### 1. Functional Query Engine
The core logic utilizes a "Stream Transformer" pattern. By treating query options as functional interfaces, the system allows for lazy, composable data transformations.
* **Abstraction:** Use of `Stream<T>` to handle filtering, sorting, and mapping.
* **Higher-Order Functions:** Implementing `apply` methods to pipe logic into stream pipelines.

### 2. Design Patterns
Mastery of common design patterns to ensure memory efficiency and clean architecture:
* **Flyweight Pattern:** Shared instances for Categories and Funds to minimize heap allocation.
* **Factory Pattern:** Static factory methods (`of()`) to enforce "valid by construction" objects.
* **Facade Pattern:** Providing a simplified interface for complex ledger operations.
* **Registry Pattern:** Generic, bidirectional mapping for shared object management.

### 3. Type System & Generics
* **Generics:** Implementation of a generic `FlyweightRegistry<T>` to handle various shared entities.
* **Wildcards:** Use of bounded wildcards (`? extends T`) in factory methods for flexibility.
* **Immutability:** Extensive use of `final` fields and defensive copying to ensure data integrity.

### 4. Robust Data Handling
* **Value Objects:** Normalization of inputs (e.g., Title Case formatting) within constructor/factory logic.
* **Exception Handling:** Custom checked exceptions for domain-specific validation (e.g., date verification).
* **Standard Interfaces:** Implementation of `Comparable<T>` and `@Override toString()` for standard Java interoperability.

---

## Testing Suite
The project includes a comprehensive **JUnit 5** suite designed to verify:
* **Referential Equality:** Ensuring the Flyweight pattern returns identical memory instances.
* **Stream Integrity:** Verifying that queries return new collections without mutating the source.
* **Edge Cases:** Handling whitespace-only strings, null-like inputs, and boundary dates.

---

## Environment
* **Language:** Java 25+
* **Build Tool:** IntelliJ IDEA / Manual Compilation
* **Testing:** JUnit 5.x
* **Philosophy:** Minimalist, decoupled, and efficient.

---

> [!NOTE]
> This repository is a personal sandbox for mastering Java's functional features and software design principles ahead of the CS2030S practical assessment.