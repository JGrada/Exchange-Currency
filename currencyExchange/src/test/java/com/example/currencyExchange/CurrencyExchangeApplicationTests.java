package com.example.currencyExchange;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;
import domain.exceptions.InvalidValueException;
import infrastructure.common.ConversionHandler;
import infrastructure.persistence.cache.CacheImpl;
import infrastructure.persistence.cache.ICache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ExchangeRateServiceImpl;
import services.IExchangeRateService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;

class CurrencyExchangeApplicationTests {

	IExchangeRateService ers;

	@BeforeEach
	void setUp() {
		//Setting up what might be necessary for the tests
		ConversionHandler ch = new ConversionHandler();
		ICache cache = new CacheImpl();
		ers = new ExchangeRateServiceImpl(ch, cache);
	}

	@Test
	@DisplayName("Testing single exchange rate should work")
	void validExchangeRate() {
		// Arrange
		CurrencyCode base = CurrencyCode.EUR; //Defining the base currency for the test, could be any other one
		CurrencyCode to = CurrencyCode.AED; //Defining the to currency for the test, could be any other one

		// Act
		ExchangeRate result = ers.getExchangeRate(base, to); //Getting the exchange rate

		// Assert
		Assertions.assertEquals(base, result.getFromCurrency(), "Should be EUR base currency"); //Verifying if it checks the base currency
		Assertions.assertEquals(to, result.getToCurrency(), "Should be AED to currency");  //Verifying if it checks the to currency
		Assertions.assertInstanceOf(Double.class, result.getToAmount(), "Should be double type"); //Can only verify it is a Double, because values might change
	}
	@Test
	@DisplayName("Testing exchange rate to multiple currencies should work")
	void validMultipleExchangeRate(){
		// Arrange
		CurrencyCode base = CurrencyCode.RON; //Defining the base currency
		ArrayList<CurrencyCode> to = new ArrayList<CurrencyCode>(); //Adding a few currencies to the array, could be more, could be less or could be others
		to.add(CurrencyCode.EUR);
		to.add(CurrencyCode.CAD);
		to.add(CurrencyCode.USD);
		to.add(CurrencyCode.AED);
		to.add(CurrencyCode.ALL);

		// Act
		ArrayList<ExchangeRate> result = ers.getMultipleExchangeRates(base, to); //geting the values

		//Assert
		for(int i = 0; i < to.size(); i++){
			Assertions.assertEquals(base, result.get(i).getFromCurrency()); //Checking base currency
			Assertions.assertEquals(to.get(i), result.get(i).getToCurrency()); //Checking if the currencies are present
			Assertions.assertInstanceOf(Double.class, result.get(i).getToAmount()); //Verifying if values are double
		}

	}

	@Test
	@DisplayName("Testing single exchange converter should work")
	void validExchangeConversion(){
		// Arrange
		CurrencyCode base = CurrencyCode.USD; //Base Currency
		CurrencyCode to = CurrencyCode.CAD; //To Currency

		// Act
		ExchangeRate result = ers.exchangeCurrency(base, to, 1.2); //Testing with valid amount

		// Assert
		Assertions.assertEquals(base, result.getFromCurrency(), "Should be USD base currency"); //Checking if base checks
		Assertions.assertEquals(to, result.getToCurrency(), "Should be CAD to currency"); //Checking if to checks
		Assertions.assertInstanceOf(Double.class, result.getToAmount(), "Should be double type"); //Verifying if it is istance of Double
	}

	@Test
	@DisplayName("Testing single exchange converter when negative value should not work")
	void invalidExchangeConversionNonPositiveAmount(){
		// Arrange
		CurrencyCode base = CurrencyCode.USD; //Base Currency
		CurrencyCode to = CurrencyCode.CAD; //To Currency

		// Act & Assert
		Assertions.assertThrows(InvalidValueException.class, () -> ers.exchangeCurrency(base, to, -1.2)); //Should throw exception because number in invalid
	}

	@Test
	@DisplayName("Testing exchange rate to all currencies should work")
	void validAllExchangeRate(){
		// Arrange
		CurrencyCode base = CurrencyCode.RON; //Setting base currency
		ArrayList<CurrencyCode> to = new ArrayList<CurrencyCode>();
		to = new ArrayList<CurrencyCode>(List.of(CurrencyCode.values())); //Inserting all the currencies available on the array

		// Act
		ArrayList<ExchangeRate> result = ers.getAllExchangeRates(base); //Getting the result from the operation

		for(int i = 0; i < to.size(); i++){
			Assertions.assertEquals(base, result.get(i).getFromCurrency()); //Verifying Base Currency
			Assertions.assertTrue(to.contains(result.get(i).getToCurrency())); //Verifying to currencies
			Assertions.assertInstanceOf(Double.class, result.get(i).getToAmount()); //Verifying if it is instance of Double
		}


	}

}
