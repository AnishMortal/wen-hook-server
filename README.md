# How to Run the Application

This guide provides the steps to set up the prerequisites, build the project, and run the application.

## 1. Prerequisites

Before you begin, ensure you have the following software installed and configured on your system:

*   **Java Development Kit (JDK) 11**: The project is configured for JDK 11. Using a different version may lead to unexpected errors.
*   **Apache Maven**: Required to build the project and manage dependencies.

## 2. Build and Run

Follow these steps in your terminal or command prompt.

1.  **Navigate to the Project Directory**:
    Open your terminal and change to the root directory of the project (the folder containing the `pom.xml` file).

    ```
    cd path/to/your/project
    ```

2.  **Build the Application with Maven**:
    Run the following command to compile the code and package it into an executable JAR file.

    ```
    mvn clean package
    ```
    This will create a `java-qualifier-0.0.1-SNAPSHOT.jar` file inside a new `target/` directory.

3.  **Execute the Application**:
    Use the following command to run the packaged JAR file. The application will start and perform its tasks automatically.

    ```
    java -jar target/java-qualifier-0.0.1-SNAPSHOT.jar
    ```
    Observe the console for output, which will show the status of the API calls and the final result.
