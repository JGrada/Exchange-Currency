package domain.exceptions;

public class InvalidCurrencyException extends BaseException{
    //Exception that will be thrown when there's a currency code that is not found on the Enum, either because it's null or because
    //it is invalid
    public InvalidCurrencyException(String message) {
        super(message);
    }
}
