# Projext: ClusteredDataWarehouse
- Description: Suppose you are part of a scrum team developing data warehouse for Bloomberg to analyze FX deals. One of customer stories is to accept deals details from and persist them into DB.

- Request logic as following:
1- Request Fields(Deal Unique Id, From Currency ISO Code "Ordering Currency", To Currency ISO Code, Deal timestamp, Deal Amount in ordering currency).
2- Validate row structure.(e.g: Missing fields, Type format..etc. We do not expect you to cover all possible cases but we'll look to how you'll implement validations)
3- System should not import same request twice.
4- No rollback allowed, what every rows imported should be saved in DB.

# ---------------------------

# Classes and Functions.
# ----------------------
# Class: Main.
- The Main class serves as the entry point for the program and demonstrates the usage of the DatabaseConnection class. It establishes a database connection, performs some operations, and then disconnects from the database. It handles any exceptions that may occur during the process and logs error messages using the Logger object.
# Functionality.
1- public static void main(String[] args): This is the entry point of the Java program. It contains the main method where the program execution starts.
- It creates an instance of the DatabaseConnection class called dbConnection.
- It creates a Logger object to log messages related to the database connection.
- It defines the DB_URL, DB_UserName, and DB_Password variables with the necessary database connection information.
- It wraps the database connection and disconnection code in try-catch blocks to handle any potential SQLException that may occur during the process.
- If an exception occurs during the connection or disconnection, it prints the stack trace and logs an appropriate error message using the Logger object.

# ---------------------------
# Class: DatabaseConnection.
- The DatabaseConnection class provides a basic implementation for establishing and managing a database connection. It allows clients to connect to a database, disconnect from it, and check the connection status.
# Functionality.
1- public DatabaseConnection(): This is the constructor of the class. It initializes the connection variable to null.
2- public void connect(String DB_URL, String DB_UserName, String DB_Password) throws SQLException: This method is used to establish a connection to the database using the provided URL, username, and password. It uses the DriverManager.getConnection method to establish the connection. If an error occurs during the connection process, it throws a SQLException.
3- public void disconnect() throws SQLException: This method is responsible for disconnecting from the database. It checks if the connection object is not null and then closes the connection using the close method. If an error occurs during the disconnection process, it throws a SQLException.
4- public Connection getConnection(): This method returns the connection object, allowing other classes or methods to access the established database connection.
5- public boolean isConnected(): This method checks if the connection object is not null, indicating that a connection has been established. It returns true if the connection is not null, and false otherwise.

# ---------------------------
# Class: RequestHandling.
- The RequestHandling class represents a request object with various attributes. It provides methods to validate the request, check for duplicate requests in the database, and persist the request into the database.
# Functionality.
1- public RequestHandling(String dealUniqueId, String fromCurrencyISOCode, String toCurrencyISOCode, Timestamp dealTimestamp, BigDecimal dealAmount): This is the constructor of the class. It initializes the instance variables with the provided values.
2- Getter and Setter methods: The class provides getter and setter methods for each of the instance variables (dealUniqueId, fromCurrencyISOCode, toCurrencyISOCode, dealTimestamp, dealAmount) to access and modify their values.
3- public boolean isValid(): This method checks if the request handling object has valid values for all the instance variables. It returns true if all the variables are non-null and non-empty, and false otherwise.
4- public boolean isDuplicateRequest(DatabaseConnection dbConnection): This method checks if the current dealUniqueId already exists in the database. It executes a SELECT query to count the number of rows with the same dealUniqueId in the "deals" table. If the count is greater than 0, it indicates a duplicate request, and the method returns true. Otherwise, it returns false.
5- public boolean persist(DatabaseConnection dbConnection) throws SQLException: This method persists the request handling object into the database. It checks if the provided dbConnection is connected to the database. If not, it logs an error message and throws a SQLException. Otherwise, it prepares an INSERT query and sets the values of the instance variables in the prepared statement. It executes the statement using executeUpdate() and checks the number of rows affected. If the number of rows affected is greater than 0, it logs a success message and returns true. Otherwise, it logs a failure message and returns false. If any SQL error occurs during the process, it logs the error message and re-throws the SQLException.

# ---------------------------
# Class: ClusteredDataWarehouseTest.
- The ClusteredDataWarehouseTest class is a JUnit test class that tests the functionality of the RequestHandling class. It sets up the necessary environment, performs various tests, and verifies the expected behavior of the persist() method in different scenarios.
# Functionality.
1- setup(): This method is annotated with @BeforeEach, which means it will be executed before each test case. It sets up the database connection and creates a test table by executing SQL statements.
2- cleanup(): This method is annotated with @AfterEach, which means it will be executed after each test case. It drops the test table and closes the database connection.
3- testPersist_SuccessfulPersistence(): This method is annotated with @Test and tests the successful persistence of a deal request. It creates a test deal request, calls the persist() method on the request handling object, and asserts that the method returns true to indicate successful persistence.
4- testPersist_ConnectionNotEstablished(): This method is annotated with @Test and tests the case where the database connection is not established. It creates a test deal request, disconnects the database connection, and asserts that calling the persist() method on the request handling object throws a SQLException.
5- testPersist_UnsuccessfulPersistence(): This method is annotated with @Test and tests the unsuccessful persistence of a deal request. It creates a test deal request, intentionally asserts false (which should fail the test), and leaves the persist() method call commented out.
6- createTestTable(): This private method creates a test table in the database using an SQL statement.
7- dropTestTable(): This private method drops the test table from the database using an SQL statement.
8- insertDealIntoTestTable(): This private method inserts a deal record into the test table. It takes the uniqueId, fromCurrency, toCurrency, timestamp, and amount as parameters and executes an SQL INSERT statement with these values.
9- createTestDealRequest(): This private method creates a test deal request object and initializes its attributes with test values. It returns the created request handling object.

# ---------------------------
# File: Dockerfile.
- The Dockerfile is a text file that contains a set of instructions for Docker to build an image. 
- The given Dockerfile specifies the steps to build a Docker image for the application.
- The Dockerfile builds an image that sets up the necessary environment and dependencies for running the Java application and defines the command to execute the application when the container is launched.
# Functionality.
1- FROM openjdk:11-jdk-slim: This instruction sets the base image for the Docker image, which in this case is an OpenJDK 11 image based on the slim version of the operating system.
2- WORKDIR /app: This instruction sets the working directory within the Docker container to /app.
3- COPY target/classes/qusai/progresssoft/example/ /app/classes: This instruction copies the compiled classes and resources from the target/classes/qusai/progresssoft/example/ directory on the local machine to the /app/classes directory inside the Docker container.
4- ENV src/main/java/ /app/classes:/app/dependency/*: This instruction sets the environment variable src/main/java/ to /app/classes:/app/dependency/*. This could be used to configure the classpath for the application.
5- CMD ["java", "qusai.progresssoft.example.Main"]: This instruction specifies the default command to run when the container is started. It executes the Java application by running the qusai.progresssoft.example.Main class.

# ---------------------------
# File: docker-compose.yml.
- The docker-compose.yml file is used to define and configure multi-container Docker applications. It allows you to define multiple services, networks, and volumes for your application.
- The docker-compose.yml file defines two services: clustered-data-warehouse and mysql. It configures the necessary settings for each service, such as image, environment variables, ports, and volumes, to enable the deployment and interaction of the clustered-data-warehouse application with a MySQL database.
# Functionality.
1- FROM openjdk:11-jdk-slim: This instruction sets the base image for the Docker image, which in this case is an OpenJDK 11 image based on the slim version of the operating system.
2- WORKDIR /app: This instruction sets the working directory within the Docker container to /app.
3- COPY target/classes/qusai/progresssoft/example/ /app/classes: This instruction copies the compiled classes and resources from the target/classes/qusai/progresssoft/example/ directory on the local machine to the /app/classes directory inside the Docker container.
4- ENV src/main/java/ /app/classes:/app/dependency/*: This instruction sets the environment variable src/main/java/ to /app/classes:/app/dependency/*. This could be used to configure the classpath for the application.
5- CMD ["java", "qusai.progresssoft.example.Main"]: This instruction specifies the default command to run when the container is started. It executes the Java application by running the qusai.progresssoft.example.Main class.

# ---------------------------
# File: Makefile.
-The Makefile is a file used to automate tasks and build processes. It defines various commands and their associated actions to be executed when invoked.
-The Makefile defines targets such as build, run, and clean, each with their respective commands to perform specific actions. It provides an organized and automated way to build, run, and clean the ClusteredDataWarehouse application.
# Functionality.
1- default: build: This sets the default command to be executed when running make without any specific target. In this case, it will execute the build target.
2- build: This target specifies the actions to be performed to build the application.
* ./mvnw clean package: This command executes the clean and package goals of the Maven wrapper (mvnw), which is a build and dependency management tool for Java projects. It cleans the project by removing any existing build artifacts and then packages the application into a JAR file.
3- run: This target specifies the actions to be performed to run the application.
* java -jar target/ClusteredDataWarehouse-1.0.jar: This command runs the Java application by executing the JAR file generated in the build step. It launches the application using the java command and specifies the JAR file to be executed.
4- clean: This target specifies the actions to be performed to clean the project.
* ./mvnw clean: This command executes the clean goal of the Maven wrapper, which removes any existing build artifacts and cleans the project.
