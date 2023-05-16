package service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

@RestController
public class ConverterController {
    @GetMapping("/api/convert")
    public  <JsonElement> CurrencyConverterResponse converter(@RequestParam String from, @RequestParam String to, @RequestParam(required = false) Double amount) throws IOException, ParseException {
        if(amount == null || amount == 0){
            amount = 1.0;
        } //VALE A PENA PASSAR PARA UMA FUNÇÃO ?????????
        if(Objects.equals(from, "")){
            from = "EUR";
        }
        if(Objects.equals(to, "")){
            to = "USD";
        }
        System.out.println(from);
        System.out.println(to);
        String urlStr = "https://api.exchangerate.host/convert?from=" + from + "&to=" + to + "&amount=" + amount;
        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JSONParser jsonParser = new JSONParser();
        JsonElement jsonElement = (JsonElement) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

        Number exchangableAmount = (Number) ((JSONObject) ((Map<?, ?>) jsonElement).get("query")).get("amount");
        Double result = (Double) ((Map<?, ?>) jsonElement).get("result");
        String currencyFrom = (String) ((JSONObject) ((Map<?, ?>) jsonElement).get("query")).get("from");
        String currencyTo = (String) ((JSONObject) ((Map<?, ?>) jsonElement).get("query")).get("to");

        //return new CurrencyConverterResponse(exchangableAmount, result, currencyFrom, currencyTo);
        return new CurrencyConverterResponse(result, currencyFrom, currencyTo, exchangableAmount);
    }
}