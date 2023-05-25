package infrastructure.controllers;

import error.ErrorResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import deprecated.SuppliedCurrencysResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MupltipleRatesController {
    @GetMapping("/api/latest")
    public <JsonElement> SuppliedCurrencysResponse suppliedCurrencysViewer(@RequestParam String base, @RequestParam ArrayList<String> symbols) throws IOException, ParseException {
        if(symbols.isEmpty()){
            throw new NoSuppliedCurrencysException("There were no supplied currencies");
        }
        String urlStr = "https://api.exchangerate.host/latest?base=" + base + "&symbols=" + symbols;

        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JSONParser jsonParser = new JSONParser();
        JsonElement jsonElement = (JsonElement) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));



        String baseCurrency = base;
        JSONObject rates = (JSONObject) ((JSONObject) ((Map<?, ?>) jsonElement)).get("rates");


        Map<String, Object> exchangeRates = new HashMap<>();
        for (Object currencyCode : rates.keySet()) {
            for(String symbol : symbols){
                if(symbol.toString().equals(currencyCode.toString())){
                    Object rateValue = rates.get(currencyCode);
                    exchangeRates.put(currencyCode.toString(), rateValue);
                }
            }

        }
        return new SuppliedCurrencysResponse(base, exchangeRates);
    }


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class NonExistingCurrencyException extends RuntimeException {
        public NonExistingCurrencyException(String message) {
            super(message);
        }
    }
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class NoSuppliedCurrencysException extends RuntimeException {
        public NoSuppliedCurrencysException(String message) {
            super(message);
        }
    }

    @ControllerAdvice
    public class ExceptionHandlerAdvice {
        @ExceptionHandler(NonExistingCurrencyException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ErrorResponse handleNonExistingCurrencyException(NonExistingCurrencyException ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setPath("/api/latest");
            return errorResponse;
        }
    }
    @ControllerAdvice
    public class NoSuppliedCurrenciesExceptionHandlerAdvice {
        @ExceptionHandler(NoSuppliedCurrencysException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ErrorResponse handleNoSuppliedCurrenciesException(NoSuppliedCurrencysException ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setPath("/api/latest");
            return errorResponse;
        }
    }
}
