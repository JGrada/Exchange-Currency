package com.example.currencyExchange;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;
import domain.exceptions.InvalidCurrencyException;
import domain.exceptions.InvalidValueException;
import infrastructure.common.ConversionHandler;
import infrastructure.persistence.cache.CacheImpl;
import infrastructure.persistence.cache.ICache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import services.ExchangeRateServiceImpl;
import services.IExchangeRateService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;

class CurrencyExchangeApplicationTests {

	IExchangeRateService ers;

	@BeforeEach
	void setUp() {
		ConversionHandler ch = new ConversionHandler();
		ICache cache = new CacheImpl();
		ers = new ExchangeRateServiceImpl(ch, cache);
	}

	@Test
	@DisplayName("Testing single exchange rate should work")
	void validExchangeRate() {
		// Arrange
		CurrencyCode base = CurrencyCode.EUR;
		CurrencyCode to = CurrencyCode.AED;

		// Act
		ExchangeRate result = ers.getExchangeRate(base, to);

		// Assert
		Assertions.assertEquals(base, result.getFromCurrency(), "Should be EUR base currency");
		Assertions.assertEquals(to, result.getToCurrency(), "Should be AED to currency");
		Assertions.assertInstanceOf(Double.class, result.getToAmount(), "Should be double type");
	}
	@Test
	@DisplayName("Testing exchange rate to multiple currencies should work")
	void validMultipleExchangeRate(){
		// Arrange
		CurrencyCode base = CurrencyCode.RON;
		ArrayList<CurrencyCode> to = new ArrayList<CurrencyCode>();
		to.add(CurrencyCode.EUR);
		to.add(CurrencyCode.CAD);
		to.add(CurrencyCode.USD);
		to.add(CurrencyCode.AED);
		to.add(CurrencyCode.ALL);

		// Act
		ArrayList<ExchangeRate> result = ers.getMultipleExchangeRates(base, to);

		for(int i = 0; i < to.size(); i++){
			Assertions.assertEquals(base, result.get(i).getFromCurrency());
			Assertions.assertEquals(to.get(i), result.get(i).getToCurrency());
			Assertions.assertInstanceOf(Double.class, result.get(i).getToAmount());
		}


	}

	@Test
	@DisplayName("Testing single exchange converter should work")
	void validExchangeConversion(){
		// Arrange
		CurrencyCode base = CurrencyCode.USD;
		CurrencyCode to = CurrencyCode.CAD;

		// Act
		ExchangeRate result = ers.exchangeCurrency(base, to, 1.2);

		// Assert
		Assertions.assertEquals(base, result.getFromCurrency(), "Should be USD base currency");
		Assertions.assertEquals(to, result.getToCurrency(), "Should be CAD to currency");
		Assertions.assertInstanceOf(Double.class, result.getToAmount(), "Should be double type");
	}

	@Test
	@DisplayName("Testing single exchange converter when negative value should not work")
	void invalidExchangeConversionNonPositiveAmount(){
		// Arrange
		CurrencyCode base = CurrencyCode.USD;
		CurrencyCode to = CurrencyCode.CAD;

		// Act & Assert
		Assertions.assertThrows(InvalidValueException.class, () -> ers.exchangeCurrency(base, to, -1.2));
	}

	@Test
	@DisplayName("Testing exchange rate to all currencies should work")
	void validAllExchangeRate(){
		// Arrange
		CurrencyCode base = CurrencyCode.RON;
		ArrayList<CurrencyCode> to = new ArrayList<CurrencyCode>();
		to = new ArrayList<CurrencyCode>(List.of(CurrencyCode.values()));

		// Act
		ArrayList<ExchangeRate> result = ers.getAllExchangeRates(base);

		for(int i = 0; i < to.size(); i++){
			Assertions.assertEquals(base, result.get(i).getFromCurrency());
			Assertions.assertTrue(to.contains(result.get(i).getToCurrency()));
			Assertions.assertInstanceOf(Double.class, result.get(i).getToAmount());
		}


	}

}
