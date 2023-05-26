package infrastructure.common;

import domain.enums.CurrencyCode;
import domain.exceptions.InvalidCurrencyException;

import java.util.List;
import java.util.stream.Stream;

public class ConversionHandler {
    //Class responsible for converting Strings into Currency Codes
    //So that it can be compared with the Codes on the enum
    public CurrencyCode toCurrencyCode(String currencyString) throws InvalidCurrencyException {

        if (currencyString == null || currencyString.equals("") || !Stream.of(CurrencyCode.values()).map(Enum::toString).toList().contains(currencyString)) {
            throw new InvalidCurrencyException("Currency " + currencyString +  " is invalid.");
        }

        return CurrencyCode.valueOf(currencyString);
    }
}
