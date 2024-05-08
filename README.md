# Library-Inquiry-System

The Library Inquiry System is a project that aims to create a comprehensive system for managing information about books in a library, library users, and loans. The system provides interactive operations for operators and is implemented using Java JDBC API for database access.

# Functionality
The Library Inquiry System supports the following features:

Database Design: It is required to design the database for the system, including an ER-diagram and a relational schema.

Java Command Line Application: Implement the Library Inquiry System as a Java command-line program, and the use Java JDBC API to connect to a MySQL database system. 

# Files
The project consists of the following files:
JavaSQL.java: This file contains the main code for the library system.
mysql-connector-java-5.1.47.jar: This is the MySQL Connector/J library required for connecting to the MySQL database.

# Compilation and Execution
To compile and run the library system, please follow the instructions below:

Connect to the Linux server and navigate to the directory where the files and the data file are located.
Use the following commands to compile and run the system:

<code>javac JavaSQL.java
java -cp .:mysql-connector-java-5.1.47.jar JavaSQL</code>

The system will start running, and you will be presented with options for the Administrator, Library User, or Librarian operations.
