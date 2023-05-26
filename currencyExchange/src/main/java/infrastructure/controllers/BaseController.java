package infrastructure.controllers;

import domain.exceptions.InvalidCurrencyException;
import domain.exceptions.InvalidValueException;
import infrastructure.common.ConversionHandler;
import infrastructure.error.ErrorResponse;
import infrastructure.persistence.cache.CacheImpl;
import infrastructure.persistence.cache.ICache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

public abstract class BaseController {
    ConversionHandler ch = new ConversionHandler();
    ICache cache = new CacheImpl();

    //This base controller was created to handle the exceptions so that they can be extended for the other controllers
    //and also to initialize the cache method and Conversion Handler, so, with this, we can send both the cache and conversion handler
    //to every other controllers

    //Both of the following handle exceptions that will be thrown for specific user input errors so that the

    //This exception is thrown when a user inputs either a currency that is not present on the enum created or when there's a "null currency"
    //Ex: Trying to see the rate from : "NONEXISTINGCURRENCY" to "EUR"
    @ExceptionHandler(InvalidCurrencyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleNonExistingCurrencyException(HttpServletRequest req, InvalidCurrencyException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now().toString());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(req.getRequestURL().toString());
        return errorResponse;
    }

    //This exception is thrown when a user is trying to convert a non positive number
    //Ex: Trying to convert -10EUR to USD
    @ExceptionHandler(InvalidValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidValueException(HttpServletRequest req, InvalidValueException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now().toString());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(req.getRequestURL().toString());
        return errorResponse;
    }

}
