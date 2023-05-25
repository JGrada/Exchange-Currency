package services;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;
import infrastructure.common.ConversionHandler;

import java.util.ArrayList;

public interface IExchangeRateService {

    public ExchangeRate getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency);
    public ExchangeRate exchangeCurrency(CurrencyCode fromCurrency, CurrencyCode toCurrency, Double amount);
    public ArrayList<ExchangeRate> getMultipleExchangeRates(CurrencyCode fromCurrency, ArrayList<CurrencyCode> toCurrency);
    public ArrayList<ExchangeRate> getAllExchangeRates(CurrencyCode fromCurrency);


}
