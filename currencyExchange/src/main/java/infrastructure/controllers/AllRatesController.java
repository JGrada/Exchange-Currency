package infrastructure.controllers;
import error.ErrorResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import deprecated.AllRatesResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AllRatesController {
    @GetMapping("/api/rate")
    public <JsonElement> AllRatesResponse rateViewer(@RequestParam String base) throws IOException, ParseException {

        String urlStr = "https://api.exchangerate.host/viewrate?base=" + base;

        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JSONParser jsonParser = new JSONParser();
        JsonElement jsonElement = (JsonElement) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

        String baseCurrency = base;
        JSONObject rates = (JSONObject) ((JSONObject) ((Map<?, ?>) jsonElement)).get("rates");
        if(base == null){
            base = "EUR";
        }
        else if(!rates.containsKey(base)){
            throw new NonExistingCurrencyException("Non existing currency");
        }

        Map<String, Object> exchangeRates = new HashMap<>();
        for (Object currencyCode : rates.keySet()) {
            Object rateValue = rates.get(currencyCode);
            exchangeRates.put(currencyCode.toString(), rateValue);
        }
        return new AllRatesResponse(base, exchangeRates);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class NonExistingCurrencyException extends RuntimeException {
        public NonExistingCurrencyException(String message) {
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
            errorResponse.setPath("/api/rate");
            return errorResponse;
        }
    }
}
