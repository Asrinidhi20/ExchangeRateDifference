# ExchangeRateDifference

## Introduction

The Exchange Rates API provides endpoints to manage exchange rates between different currencies. The application uses Spring Boot for the RESTful API and MongoDB to store exchange rate data.

## Technologies Used

Java 17

Spring Boot 3.0

Spring Data

Restful API

MongoDB

ExchangeAPI

Lombok

Maven

## Prerequisites

Any code editor(VS code, Eclipsse, IntelliJ etc)

MongoDB

## Get API KEY 

1 ) Open and register exchangerate-api.com

2 ) Register Exchange Rates Data API and Get API key

## Design Patterns Used

## Factory Pattern:

The factory design pattern is an object-oriented design pattern that involves creating objects using a factory. A factory can be an object, class, or function that creates objects.

## Singleton Pattern:

The Singleton pattern is a creational design pattern that ensures that a class has only one object and provides a global point of access to it. This pattern is often used in Spring Boot REST API projects to create singletons for database connections, object pools, and other resources that need to be shared across the application.

## Dependency Injection (DI):

In Spring Boot, Dependency Injection (DI) is a fundamental concept that’s integral to the framework’s design. DI is primarily achieved through annotations, allowing Spring Boot to manage and inject dependencies into components or beans. 

Base URL

http://localhost:8080/api/exchangeRate

## Endpoints

 1. Get percentage of Exchange Rates Differences of a currency 

  Endpoint: /api/exchangeRate/diff?code=INR

  Method: GET

  Description: Retrieve percentage of increase/decrease in exchange rate based on USD

  Example Request: GET http://localhost:8080/api/exchangeRate/diff?code=INR

2. Get  USD Exchange Rates By Date

  Endpoint: /api/exchangeRateByDate/{date}

  Method: GET

  Description: Retrieve exchange rates for USD of a specified date.

  Example Request: GET http://localhost:8080/api/exchangeRate/exchangeRateByDate/2023-02-05

3. Create Exchange Rate

  Endpoint: /api/exchangeRate/create

  Method: POST

  Description: Create exchange rates for required currency on a specified date.

  Example Body: Provide the exchange rate as a JSON string in the request body.
  json

  Copy code

  {

    "exchangeRate": 1.23, 

    "currency": "USD",

    "date": "2023-02-05"

  }

  Example Request: POST http://localhost:8080/api/exchangeRate/create

4. Update Exchange Rate

  Endpoint: /api/exchangeRate/update/{id}

  Method: PUT

  Description: Helps us to modify already existing data

  Example Body: Provide the updated exchange rate as a JSON string in the request body.
  json

  Copy code

  {

    "exchangeRate": 1.35,

    "currency": "USD",

    "date": "2023-02-05"

  }

  Example: PUT http://localhost:8080/api/exchangeRate/update/2023-02-05

 5. Delete Exchange Rate

  Endpoint: /api/exchangeRate/delete/{id}

  Method: DELETE

  Description: Deletes the exchange rates of the currency on a specified date

  Example: DELETE http://localhost:8080/api/exchangeRate/delete/2023-02-05

  6. Get the latest exchange rates of USD  

  Endpoint: /api/exchangeRate/latest-usd

  Method: GET

  Description: Retrieve the latest exchange rates based on USD

  Example Request: GET http://localhost:8080/api/exchangeRate/latest-usd

  ## Database
  
  In this project, MongoDB is used for storing and retrieving data. Install the MongoDB locally and connect to the connection server: http://localhost:27017.
  The name of the database is exchange_rates and the name of the collection is exchange_data.Use MongoCompass GUI for MongoDB Database.

  ## Build and Run

  Install the Java version 17 and MongoDB. Register in exchangerate-api.com and get your API key. Download the project and run it on code editor. Use Postman to chek the Endpoints.

  






