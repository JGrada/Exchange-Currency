package domain.entities;

import domain.enums.CurrencyCode;

public class ExchangeRate {

    private CurrencyCode fromCurrency;
    private CurrencyCode toCurrency;
    private Double fromAmount;
    private Double toAmount;

    public ExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency, Double fromAmount, Double toAmount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
    }

    public CurrencyCode getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(CurrencyCode fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public CurrencyCode getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(CurrencyCode toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public Double getToAmount() {
        return toAmount;
    }

    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }

}
