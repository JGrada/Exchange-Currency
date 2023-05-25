package services;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;

import java.util.ArrayList;

public interface IExchangeRateService {

    public ExchangeRate getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency);
    public ExchangeRate exchangeCurrency(CurrencyCode fromCurrency, CurrencyCode toCurrency, Double amount);
    public ArrayList<ExchangeRate> getMultipleExchangeRates(CurrencyCode fromCurrency, CurrencyCode toCurrency);
    public ArrayList<ExchangeRate> getAllExchangeRates(CurrencyCode fromCurrency, CurrencyCode toCurrency);


}
