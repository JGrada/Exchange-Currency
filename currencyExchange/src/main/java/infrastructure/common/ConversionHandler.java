package infrastructure.common;

import domain.enums.CurrencyCode;
import domain.exceptions.InvalidCurrencyException;

import java.util.List;

public class ConversionHandler {

    public CurrencyCode toCurrencyCode(String currencyString) throws InvalidCurrencyException {
        CurrencyCode currencyCode = CurrencyCode.valueOf(currencyString);
        if (currencyCode == null || !List.of(CurrencyCode.values()).contains(currencyCode)) {
            throw new InvalidCurrencyException("Currency" + currencyString +  " is invalid.");
        }

        return currencyCode;
    }
}
