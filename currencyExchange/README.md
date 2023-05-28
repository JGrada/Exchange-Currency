This API gets the Exchange Rates data from an external api and then
organizes the data to endpoints on the API developed by me.

1\. The API was built with the following versions:

Java Spring Boot - Version: 3.0.6 Java - Version 17 Maven - Version
4.0.0

2\. To run the project, all the dependencies that are listed on the
pom.xml file must be installed, otherwise, the project won\'t work.

3\. Swagger usage:

Swagger provides an interface for testing. Each endpoint/operation is up
on swagger and can be tested.

The endpoint for the Swagger is: http://localhost:8080/swagger-ui/index.html#/

4\. Endpoint descriptions with example:

There\'s 4 endpoints.

4.1 /api/viewRate

Direct rate view from one currency to another.

http://localhost:8080/api/viewRate?from=USD&to=EUR

This will give the rate from USD to EUR.

4.2 /api/rate

Exchange rate from one currency to all the other available currencies on
the original API.

http://localhost:8080/api/rate?base=EUR

4.3 /api/latest

This will produce the exchange rate from one currency to multiple
currencies.

Ex: Base Currency: EUR Target Currencies: USD, CAD, RON, ALL

\'http://localhost:8080/api/latest?from=EUR&to=USD%2CCAD%2CRON%2CALL

4.4 /api/convert

This endpoint allow a direct amount conversion from one currency to
another.

http://localhost:8080/api/convert?from=EUR&to=USD&amount=5

5\. Unit testing

To run the tests, the user must use the CurrencyExchangeApllicationTests
class, this will run all the tests for the 4 services. Tests can be run
seperate and not only all at the same time.

6\. Exceptions added, for correct usage:

6.1 Non existance/null Currencies. This exception will be thrown when
the user tries to look for data on Currencies that do not exist. 6.2
Invalid amount. When the user tries to convert negative numbers, this
exception will be thrown.
