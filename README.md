## Technologies Used


- **Java 17+**
- **Spring Boot + Spring MVC**
- **PostgreSQL** for data persistence
- **Maven** (or Gradle, depending on the configuration)
- **Git** for version control
- **HNB API** for currency conversion from EUR to USD
- **Swagger** for API documentation
- **Docker** for PostgreSQL container and integration testing
- **JUnit** and **Spring Test** for writing tests

## Endpoints

The service exposes the following HTTP endpoints:

### 1. Create a Product (POST)
**URL:** `/api/products`  
**Method:** POST  
**Request Body:**
json
{
  "code": "1222213722",
  "name": "Sample Product",
  "priceEur": 100.00,
  "isAvailable": true
}

### 2. Get a Specific Product (GET)
**URL:** `/api/products/{id}`  
**Method:** GET  
**Response:**
json
{
  "id": 1,
  "code": "ABCD123456",
  "name": "Product Name",
  "priceEur": 100.0,
  "priceUsd": 107.5,
  "isAvailable": true
}

### 3. Get All Products (GET)
**URL:** `/api/products`  
**Method:** GET  
**Response:**
json
{
  "id": 147,
  "code": "4234567890",
  "name": "Product A",
  "priceEur": 100,
  "priceUsd": 109.18,
  "isAvailable": true
},
{
    "id": 148,
    "code": "1255447972",
    "name": "Sample Product",
    "priceEur": 100,
    "priceUsd": 109.18,
    "isAvailable": true
}

  
Database
The service uses PostgreSQL as the database. The following table schema is used to store the products:

Product Table

  Column	             Type	              Description
  
 **id**                SERIAL	            Primary key, auto-incremented
 **code** 	           VARCHAR(10)	      Unique code (exactly 10 characters)
 **name**              VARCHAR          	Name of the product
 **priceEur** 	       DECIMAL	          Price in EUR
 **priceEsd** 	       DECIMAL	          Price in USD (calculated dynamically)
 **isAvailable**       BOOLEAN	          Availability status of the product


## Clone this repository to your local machine:

**https://github.com/32Ivan/product-service.git**

## Configure the Database Connection in application.yml
In the src/main/resources/application.yml file, you need to configure the connection to your PostgreSQL database,
including the username and password for the database.
      ![Screenshot from 2025-03-18 21-13-35](https://github.com/user-attachments/assets/ff4a2415-d93c-4564-934d-1ec660323069)

      

## Run the Application

You can now run the application using Maven:

   **mvn spring-boot:run** 

## Access Swagger UI
Once the application is running, you can access the Swagger UI for interactive API documentation and testing at:

  **http://localhost:8081/swagger-ui/index.html#**

## Docker Integration for Testing
This project uses Docker to run a PostgreSQL container for integration testing. 
By using the @SpringBootTest annotation, the integration tests will automatically interact with a running PostgreSQL container.
Run the tests using Maven:

  **mvn test**

## Performance Testing

Requests      [total, rate, throughput]         10000, 1000.02, 999.91
Duration      [total, attack, wait]             10.001s, 10s, 1.174ms
Latencies     [min, mean, 50, 90, 95, 99, max]  756.502µs, 1.332ms, 1.117ms, 1.621ms, 2.01ms, 6.128ms, 24.077ms
Bytes In      [total, mean]                     10720000, 1072.00
Bytes Out     [total, mean]                     0, 0.00
Success       [ratio]                           100.00%
Status Codes  [code:count]                      200:10000 
![vegeta-plot (3)](https://github.com/user-attachments/assets/35acece3-709c-4184-b20b-af9ce2e1b614)
