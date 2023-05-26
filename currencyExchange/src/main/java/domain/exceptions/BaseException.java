package domain.exceptions;

public abstract class BaseException extends RuntimeException{
    //Base exception that will provide a clearer aproach for more specific exceptions with specific error messages
    public BaseException(String message) {
        super(message);
    }
}
