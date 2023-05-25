package infrastructure.common;

import domain.enums.CurrencyCode;
import domain.exceptions.InvalidCurrencyException;

import java.util.List;
import java.util.stream.Stream;

public class ConversionHandler {

    public CurrencyCode toCurrencyCode(String currencyString) throws InvalidCurrencyException {

        if (currencyString == null || currencyString.equals("") || !Stream.of(CurrencyCode.values()).map(Enum::toString).toList().contains(currencyString)) {
            throw new InvalidCurrencyException("Currency " + currencyString +  " is invalid.");
        }

        CurrencyCode currencyCode = CurrencyCode.valueOf(currencyString);

        return currencyCode;
    }
}
